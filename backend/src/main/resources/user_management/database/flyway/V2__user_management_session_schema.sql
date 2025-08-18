CREATE TABLE IF NOT EXISTS user_management.refresh_tokens
(
    id         UUID PRIMARY KEY,
    user_id    UUID NOT NULL,
    shared_secret VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_management.users (id) ON DELETE CASCADE
);
CREATE INDEX idx_refresh_token_user_id ON user_management.refresh_tokens (user_id);