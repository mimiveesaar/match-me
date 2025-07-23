package tech.kood.match_me.user_management.models;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a user in the system.
 *
 * @param id        Unique identifier for the user. Cannot be null or blank.
 * @param email     The user's username. Cannot be null or blank.
 * @param email     The user's email address. Cannot be null or blank.
 * @param password  The user's hashed password. Cannot be null.
 * @param createdAt Timestamp when the user was created. Automatically set to
 *                  the current time.
 * @param updatedAt Timestamp when the user was last updated. Automatically set
 *                  to the current
 * @throws IllegalArgumentException if any parameter is invalid (null, blank, or
 *                                  non-positive where not allowed).
 */
public record User(
        UUID id,
        String username,
        String email,
        HashedPassword password,
        Instant createdAt,
        Instant updatedAt) {
    public User {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("CreatedAt cannot be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("UpdatedAt cannot be null");
        }
    }
}
