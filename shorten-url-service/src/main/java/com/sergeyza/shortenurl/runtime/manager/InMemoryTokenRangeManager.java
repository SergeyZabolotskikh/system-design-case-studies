package com.sergeyza.shortenurl.runtime.manager;

import org.springframework.stereotype.Component;

@Component
public class InMemoryTokenRangeManager {

    private long current;
    private long end;
    private long prefetchThreshold;

    public synchronized void setNewRange(long start, long end) {
        this.current = start;
        this.end = end;
        this.prefetchThreshold = start + Math.round((end - start + 1) * 0.9); // 90% threshold
    }

    public synchronized String getNextToken(String instanceId) {
        if (current > end) {
            throw new IllegalStateException("Token range exhausted — need to fetch new range");
        }
        long tokenNumber = current++;
        return instanceId + "-" + tokenNumber;
    }

    public synchronized boolean needsPrefetch() {
        return current >= prefetchThreshold;
    }

    public synchronized boolean isExhausted() {
        return current > end;
    }

    public synchronized long getRemainingTokens() {
        return end - current + 1;
    }
}
