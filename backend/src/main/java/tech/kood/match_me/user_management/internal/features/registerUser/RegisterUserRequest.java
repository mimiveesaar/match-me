package tech.kood.match_me.user_management.internal.features.registerUser;

import java.util.Optional;

import lombok.Builder;

@Builder
public record RegisterUserRequest(
    String username,
    String password,
    String email,
    Optional<String> tracingId
) {
    public RegisterUserRequest {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        } 
    }
}