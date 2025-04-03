package com.sergeyza.shortenurl.persistence.service;

import com.sergeyza.shortenurl.persistence.entity.TokenAllocationEntity;
import com.sergeyza.shortenurl.persistence.repository.TokenAllocationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of token range allocation logic using a PostgreSQL sequence.
 */
@Service
public class TokenAllocationPersistenceServiceImpl implements TokenAllocationPersistenceServiceInterface {

    private static final int TOKEN_BLOCK_SIZE = 1000;

    private final JdbcTemplate jdbcTemplate;
    private final TokenAllocationRepository tokenRepo;

    public TokenAllocationPersistenceServiceImpl(JdbcTemplate jdbcTemplate,
                                                 TokenAllocationRepository tokenRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.tokenRepo = tokenRepo;
    }

    @Override
    public TokenAllocationEntity allocateNextTokenRange(String instanceId, String region) {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('url_shortener.token_sequence')", Long.class);
        long start = (nextVal - 1) * TOKEN_BLOCK_SIZE;
        long end = nextVal * TOKEN_BLOCK_SIZE - 1;

        TokenAllocationEntity entity = new TokenAllocationEntity();
        entity.setInstanceId(instanceId);
        entity.setRegion(region);
        entity.setTokenStart(start);
        entity.setTokenEnd(end);
        entity.setAllocatedAt(LocalDateTime.now());

        return tokenRepo.save(entity);
    }

    @Override
    public List<TokenAllocationEntity> getAllocationsByInstance(String instanceId) {
        return tokenRepo.findByInstanceId(instanceId);
    }
}