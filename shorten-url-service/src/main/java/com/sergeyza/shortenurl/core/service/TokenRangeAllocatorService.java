package com.sergeyza.shortenurl.core.service;

import com.sergeyza.shortenurl.persistence.entity.TokenAllocationEntity;
import com.sergeyza.shortenurl.persistence.service.TokenAllocationPersistenceServiceInterface;
import com.sergeyza.shortenurl.runtime.manager.InMemoryTokenRangeManager;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class TokenRangeAllocatorService {

    private final InMemoryTokenRangeManager tokenRangeManager;
    private final TokenAllocationPersistenceServiceInterface persistenceService;

    private final String fullInstanceId;
    private final String awsRegion;

    public TokenRangeAllocatorService(InMemoryTokenRangeManager tokenRangeManager, TokenAllocationPersistenceServiceInterface persistenceService,
                                      @Value("${shortener.instance-id}") String instanceId, @Value("${shortener.region}") String region) {
        this.tokenRangeManager = tokenRangeManager;
        this.persistenceService = persistenceService;
        this.awsRegion = region;
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.fullInstanceId = String.format("%s-%s-%s", region, instanceId, timestamp);
    }

    @PostConstruct
    public void init() {
        allocateNewRange();
    }

    public String getNextToken() {
        if (tokenRangeManager.isExhausted()) {
            allocateNewRange();
        } else if (tokenRangeManager.needsPrefetch()) {
            new Thread(this::allocateNewRange).start();
        }
        return tokenRangeManager.getNextToken(fullInstanceId);
    }

    private synchronized void allocateNewRange() {
        TokenAllocationEntity range = persistenceService.allocateNextTokenRange(fullInstanceId, this.awsRegion);
        tokenRangeManager.setNewRange(range.getTokenStart(), range.getTokenEnd());
    }
}
