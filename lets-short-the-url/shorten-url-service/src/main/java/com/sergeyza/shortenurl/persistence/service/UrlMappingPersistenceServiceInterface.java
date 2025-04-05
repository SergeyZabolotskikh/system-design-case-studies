package com.sergeyza.shortenurl.persistence.service;

import com.sergeyza.shortenurl.persistence.entity.UrlMapEntity;

import java.util.Optional;

/**
 * Interface for accessing and storing URL mappings.
 */
public interface UrlMappingPersistenceServiceInterface {
    Optional<UrlMapEntity> findByUrlHash(String urlHash);
    UrlMapEntity saveMapping(String urlHash, String longUrl, String shortToken);
}