package tech.kood.match_me.user_management.internal.database.entities;

import java.time.Instant;
import java.util.UUID;

public record RefreshTokenEntity(
    UUID id,
    UUID userId,
    String token,
    Instant createdAt,
    Instant expiresAt
) {
    public RefreshTokenEntity {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or blank");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at timestamp cannot be null");
        }
        if (expiresAt == null) {
            throw new IllegalArgumentException("Expires at timestamp cannot be null");
        }
    }

    public RefreshTokenEntity withToken(String token) {
        return new RefreshTokenEntity(id, userId, token, createdAt, expiresAt);
    }

    public RefreshTokenEntity withExpiresAt(Instant expiresAt) {
        return new RefreshTokenEntity(id, userId, token, createdAt, expiresAt);
    }

    public RefreshTokenEntity withCreatedAt(Instant createdAt) {
        return new RefreshTokenEntity(id, userId, token, createdAt, expiresAt);
    }

    public RefreshTokenEntity withUserId(UUID userId) {
        return new RefreshTokenEntity(id, userId, token, createdAt, expiresAt);
    }

    public RefreshTokenEntity withId(UUID id) {
        return new RefreshTokenEntity(id, userId, token, createdAt, expiresAt);
    }
}