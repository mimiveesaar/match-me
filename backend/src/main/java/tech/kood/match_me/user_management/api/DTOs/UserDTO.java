package tech.kood.match_me.user_management.api.DTOs;

import java.util.UUID;

import lombok.Builder;

@Builder
public record UserDTO(
    UUID id,
    String username,
    String email
) {
    public UserDTO {
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