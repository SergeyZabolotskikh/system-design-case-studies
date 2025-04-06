SET search_path TO users_schema;

CREATE TABLE IF NOT EXISTS oauth_users (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    external_provider VARCHAR(50) NOT NULL,
    external_user_id VARCHAR(255) NOT NULL,
    access_token TEXT,
    refresh_token TEXT,
    token_expiry TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (external_provider, external_user_id)
);