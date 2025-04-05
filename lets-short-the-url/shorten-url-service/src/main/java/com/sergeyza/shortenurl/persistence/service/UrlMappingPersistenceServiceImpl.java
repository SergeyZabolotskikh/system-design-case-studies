package com.sergeyza.shortenurl.persistence.service;

import com.sergeyza.shortenurl.persistence.entity.UrlMapEntity;
import com.sergeyza.shortenurl.persistence.repository.UrlMapRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of URL mapping persistence using PostgreSQL.
 */
@Service
public class UrlMappingPersistenceServiceImpl implements UrlMappingPersistenceServiceInterface {

    private final UrlMapRepository urlMapRepository;

    public UrlMappingPersistenceServiceImpl(UrlMapRepository urlMapRepository) {
        this.urlMapRepository = urlMapRepository;
    }

    @Override
    public Optional<UrlMapEntity> findByUrlHash(String urlHash) {
        return urlMapRepository.findByUrlHash(urlHash);
    }

    @Override
    public UrlMapEntity saveMapping(String urlHash, String longUrl, String shortToken) {
        UrlMapEntity entity = new UrlMapEntity();
        entity.setUrlHash(urlHash);
        entity.setLongUrl(longUrl);
        entity.setShortToken(shortToken);
        entity.setCreatedAt(LocalDateTime.now());
        return urlMapRepository.save(entity);
    }
}