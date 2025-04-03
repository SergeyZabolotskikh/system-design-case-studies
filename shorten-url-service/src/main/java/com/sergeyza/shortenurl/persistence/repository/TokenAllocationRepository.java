package com.sergeyza.shortenurl.persistence.repository;

import com.sergeyza.shortenurl.persistence.entity.TokenAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing token allocation ranges for instances.
 */
@Repository
public interface TokenAllocationRepository extends JpaRepository<TokenAllocationEntity, Long> {
    List<TokenAllocationEntity> findByInstanceId(String instanceId);
}