package com.sergeyza.shortenurl.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class mapping to the 'token_allocations' table.
 * Represents pre-allocated token ranges for a specific instance/region.
 */
@Entity
@Table(name = "token_allocations", schema = "url_shortener")
public class TokenAllocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allocation_id")
    private Long id;

    @Column(name = "instance_id", nullable = false)
    private String instanceId;

    @Column(name = "region")
    private String region;

    @Column(name = "token_start", nullable = false)
    private Long tokenStart;

    @Column(name = "token_end", nullable = false)
    private Long tokenEnd;

    @Column(name = "allocated_at")
    private LocalDateTime allocatedAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getTokenStart() {
        return tokenStart;
    }

    public void setTokenStart(Long tokenStart) {
        this.tokenStart = tokenStart;
    }

    public Long getTokenEnd() {
        return tokenEnd;
    }

    public void setTokenEnd(Long tokenEnd) {
        this.tokenEnd = tokenEnd;
    }

    public LocalDateTime getAllocatedAt() {
        return allocatedAt;
    }

    public void setAllocatedAt(LocalDateTime allocatedAt) {
        this.allocatedAt = allocatedAt;
    }
}