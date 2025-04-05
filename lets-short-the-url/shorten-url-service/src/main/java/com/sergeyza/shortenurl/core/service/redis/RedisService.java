package com.sergeyza.shortenurl.core.service.redis;

import com.sergeyza.shortenurl.persistence.entity.UrlMapEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, UrlMapEntity> redisTemplate;
    private final ValueOperations<String, UrlMapEntity> valueOps;
    private final long ttlSeconds;

    public RedisService(RedisTemplate<String, UrlMapEntity> redisTemplate, @Value("${shortener.cache.ttl-seconds}") long ttlSeconds) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        this.ttlSeconds = ttlSeconds;
    }

    public Optional<UrlMapEntity> get(String key) {
        UrlMapEntity entity = valueOps.get(key);
        return Optional.ofNullable(entity);
    }

    public void put(String urlHash, UrlMapEntity entity) {
        redisTemplate.opsForValue().set(urlHash, entity, ttlSeconds, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(entity.getShortToken(), entity, ttlSeconds, TimeUnit.SECONDS);
    }
}
