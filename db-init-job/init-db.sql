-- Create schema
CREATE SCHEMA IF NOT EXISTS url_shortener;

-- Token allocation sequence (for numeric token generation)
CREATE SEQUENCE IF NOT EXISTS url_shortener.token_sequence
  INCREMENT BY 1
  MINVALUE 1
  START WITH 1000000
  CACHE 100;

-- URL ↔ Token Mapping Table
CREATE TABLE IF NOT EXISTS url_shortener.url_map (
    id SERIAL PRIMARY KEY,
    url_hash VARCHAR(64) UNIQUE NOT NULL,
    long_url TEXT NOT NULL,
    short_token VARCHAR(16) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for fast lookups
CREATE INDEX IF NOT EXISTS idx_url_map_token ON url_shortener.url_map(short_token);
CREATE INDEX IF NOT EXISTS idx_url_map_hash ON url_shortener.url_map(url_hash);

-- Token Allocation Metadata Table
CREATE TABLE IF NOT EXISTS url_shortener.token_allocations (
    allocation_id SERIAL PRIMARY KEY,
    instance_id VARCHAR(128) NOT NULL,
    region VARCHAR(64),
    token_start BIGINT NOT NULL,
    token_end BIGINT NOT NULL,
    allocated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);