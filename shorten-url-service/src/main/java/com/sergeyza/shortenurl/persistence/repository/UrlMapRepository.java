package com.sergeyza.shortenurl.persistence.repository;

import com.sergeyza.shortenurl.persistence.entity.UrlMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMapRepository extends JpaRepository<UrlMapEntity, Long> {
    Optional<UrlMapEntity> findByUrlHash(String urlHash);
}