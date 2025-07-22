package tech.kood.match_me.user_management.models;

import java.time.Instant;
import java.util.UUID;

public record RefreshToken(UUID id, UUID userId, String token, Instant createdAt, Instant expiresAt) {

    public RefreshToken {
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

    public RefreshToken withToken(String token) {
        return new RefreshToken(id, userId, token, createdAt, expiresAt);
    }

    public RefreshToken withExpiresAt(Instant expiresAt) {
        return new RefreshToken(id, userId, token, createdAt, expiresAt);
    }

    public RefreshToken withCreatedAt(Instant createdAt) {
        return new RefreshToken(id, userId, token, createdAt, expiresAt);
    }

    public RefreshToken withUserId(UUID userId) {
        return new RefreshToken(id, userId, token, createdAt, expiresAt);
    }

    public RefreshToken withId(UUID id) {
        return new RefreshToken(id, userId, token, createdAt, expiresAt);
    }
}