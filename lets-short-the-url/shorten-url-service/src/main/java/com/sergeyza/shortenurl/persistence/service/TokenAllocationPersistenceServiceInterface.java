package com.sergeyza.shortenurl.persistence.service;

import com.sergeyza.shortenurl.persistence.entity.TokenAllocationEntity;

import java.util.List;

/**
 * Interface for managing token allocation operations.
 */
public interface TokenAllocationPersistenceServiceInterface {
    TokenAllocationEntity allocateNextTokenRange(String instanceId, String region);
    List<TokenAllocationEntity> getAllocationsByInstance(String instanceId);
}