package com.sergeyza.shortenurl.core.service;

import com.sergeyza.shortenurl.persistence.entity.UrlMapEntity;
import com.sergeyza.shortenurl.persistence.service.UrlMappingPersistenceServiceInterface;
import com.sergeyza.shortenurl.util.TokenEncoder;
import com.sergeyza.shortenurl.core.service.redis.RedisService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class ShortenUrlService {

    private final UrlMappingPersistenceServiceInterface urlPersistence;
    private final TokenRangeAllocatorService tokenAllocator;
    private final RedisService redisService;

    public ShortenUrlService(UrlMappingPersistenceServiceInterface urlPersistence, TokenRangeAllocatorService tokenAllocator, RedisService redisService) {
        this.urlPersistence = urlPersistence;
        this.tokenAllocator = tokenAllocator;
        this.redisService = redisService;
    }


    /**
     * Shortens the provided long URL into a unique 6-character token.
     * This method follows a safe and scalable approach to URL shortening:
     **** Computes an SHA-256 hash of the input URL to ensure consistent fingerprinting.
     **** Checks the database to see if this URL has already been shortened using the hash as a lookup key.
     **** If found, returns the existing mapping.
     **** If not found, requests a globally unique token from the allocator (which includes instance ID, region, timestamp, DB sequence number allocated to instance).
     **** The token is hashed and converted into a compact Base62-encoded short string.
     **** The mapping is saved in the database and returned.
     * In case of a race condition where two instances attempt to insert the same hash concurrently,
     * the method will gracefully catch a unique constraint violation, wait briefly, and retry fetching
     * the already-inserted mapping.
     */
    public UrlMapEntity shortenUrl(String longUrl) {
        String urlHash = sha256(longUrl); // Step 1: Create a deterministic hash
        //Check in Redis cache first
        Optional<UrlMapEntity> cached = redisService.get(urlHash);
        if (cached.isPresent()) {
            return cached.get(); //Cache hit
        }
        //Check in DB (local Postgres or global DynamoDB)
        Optional<UrlMapEntity> dbResult = urlPersistence.findByUrlHash(urlHash);
        if (dbResult.isPresent()) {
            redisService.put(urlHash, dbResult.get()); // Sync cache with DB
            return dbResult.get();                     // Cache missed, but we have in the DB
        }
        //Not found → Generate new token and save
        try {
            String tokenNumber = tokenAllocator.getNextToken(); // Step 2: Globally unique token ID (with region, instance, timestamp, token from range)
            String shortToken = TokenEncoder.encode(hashToLong(tokenNumber), 6); // Step 3: Hash the token string → numeric → Base62 → short token
            UrlMapEntity newMapping = urlPersistence.saveMapping(urlHash, longUrl, shortToken); // Step 4: Save and return
            redisService.put(urlHash, newMapping);
            return newMapping;
        } catch (DataIntegrityViolationException e) {
            //Another instance was the first to insert the unique urlHash, wait, and try to fetch from the DB again
            try {
                Thread.sleep(500);
                Optional<UrlMapEntity> newResult = urlPersistence.findByUrlHash(urlHash);
                if (newResult.isPresent()) {
                    redisService.put(urlHash, newResult.get()); // Sync cache with DB
                    return newResult.get();                     // Cache missed, but we have in the DB
                }
            } catch (InterruptedException waitException) {
                throw new RuntimeException(waitException.getMessage());
            } catch (IllegalStateException dbException) {
                throw new IllegalStateException("Insert failed and URL still not found!");
            }
        }
        return null;
    }

    /**
     * Computes an SHA-256 hash of the given long URL.
     * The hash is used to check if a given long URL already exists in the database to avoid duplication.
     */
    private String sha256(String longUrl) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(longUrl.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException("SHA-256 hashing failed", exception);
        }
    }


    /**
     * Converts a string (typically a unique token or long URL) into a positive long value using SHA-256 hashing and extracting the first 8 bytes.
     * <p>This method is used to produce a numeric value suitable for base62 encoding into a compact short token.
     */
    public long hashToLong(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            // Take the first 8 bytes and convert to long
            long value = 0;
            for (int i = 0; i < 8; i++) {
                value = (value << 8) | (hashBytes[i] & 0xff);
            }
            return Math.abs(value);
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException("Failed to hash token string", exception);
        }
    }

}
