CREATE TABLE refresh_tokens
(
    id         UUID PRIMARY KEY,
    user_id    UUID NOT NULL,
    token      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_management.users (id) ON DELETE CASCADE
);
CREATE INDEX idx_refresh_token_user_id ON refresh_tokens (user_id);