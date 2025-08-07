package tech.kood.match_me.user_management.internal.domain.features.registerUser;

import jakarta.annotation.Nullable;

/**
 * Represents a request to register a new user in the system.
 *
 * @param requestId The unique internal identifier for this registration request.
 * @param email The username of the user to be registered; must not be null or blank.
 * @param password The password for the new user; must not be null or blank.
 * @param email The email address of the user; must not be null or blank.
 * @param tracingId An optional tracing identifier for request tracking.
 * @throws IllegalArgumentException If username, password, or email is null or blank.
 */
public record RegisterUserRequest(String requestId, String username, String password, String email,
        @Nullable String tracingId) {

    public RegisterUserRequest withTracingId(String newTracingId) {
        return new RegisterUserRequest(requestId, username, password, email, newTracingId);
    }

    public RegisterUserRequest withRequestId(String newRequestId) {
        return new RegisterUserRequest(newRequestId, username, password, email, tracingId);
    }

    public RegisterUserRequest withUsername(String newUsername) {
        return new RegisterUserRequest(requestId, newUsername, password, email, tracingId);
    }

    public RegisterUserRequest withPassword(String newPassword) {
        return new RegisterUserRequest(requestId, username, newPassword, email, tracingId);
    }

    public RegisterUserRequest withEmail(String newEmail) {
        return new RegisterUserRequest(requestId, username, password, newEmail, tracingId);
    }
}
