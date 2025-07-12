package tech.kood.match_me.user_management.DTOs;

import io.micrometer.common.lang.Nullable;

public record RegisterUserRequestDTO(
    String username,
    String email,
    String password,
    @Nullable String tracingId) {

    public RegisterUserRequestDTO {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (tracingId == null) {
            throw new IllegalArgumentException("Tracing ID cannot be null");
        }
    }
    
}
