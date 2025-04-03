package com.sergeyza.shortenurl.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "url_map", schema = "url_shortener")
public class UrlMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_hash", nullable = false, unique = true)
    private String urlHash;

    @Column(name = "long_url", nullable = false)
    private String longUrl;

    @Column(name = "short_token", nullable = false, unique = true)
    private String shortToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // PrePersist method to set createdAt if it's not already set
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlHash() {
        return urlHash;
    }

    public void setUrlHash(String urlHash) {
        this.urlHash = urlHash;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortToken() {
        return shortToken;
    }

    public void setShortToken(String shortToken) {
        this.shortToken = shortToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
