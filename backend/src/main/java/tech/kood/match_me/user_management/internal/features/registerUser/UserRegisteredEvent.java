package tech.kood.match_me.user_management.internal.features.registerUser;

import java.util.Optional;

public record UserRegisteredEvent(
    String userId,
    String username,
    String email,
    Optional<String> tracingId
) {
    public UserRegisteredEvent {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (tracingId == null) {
            throw new IllegalArgumentException("Tracing ID cannot be null");
        }
    }
}