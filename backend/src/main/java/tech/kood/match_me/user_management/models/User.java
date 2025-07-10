package tech.kood.match_me.user_management.models;

import java.util.UUID;

import lombok.Builder;

@Builder
public record User(
    UUID id,
    String username,
    String email
) {
    public User {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null or blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
}