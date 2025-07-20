package tech.kood.match_me.user_management.internal.features.registerUser;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents a request to register a new user in the system.
 *
 * @param requestId  The unique internal identifier for this registration request.
 * @param username   The username of the user to be registered; must not be null or blank.
 * @param password   The password for the new user; must not be null or blank.
 * @param email      The email address of the user; must not be null or blank.
 * @param tracingId  An optional tracing identifier for request tracking.
 * @throws IllegalArgumentException If username, password, or email is null or blank.
 */
public record RegisterUserRequest(
    UUID requestId,
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

    public RegisterUserRequest withTracingId(String newTracingId) {
        return new RegisterUserRequest(requestId, username, password, email, Optional.ofNullable(newTracingId));
    }

    public RegisterUserRequest withRequestId(UUID newRequestId) {
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