package tech.kood.match_me.user_management.models;

/**
 * Represents a user in the system.
 *
 * @param id         Unique identifier for the user. Cannot be null or blank.
 * @param username   The user's username. Cannot be null or blank.
 * @param email      The user's email address. Cannot be null or blank.
 * @param password   The user's hashed password. Cannot be null.
 * @param createdAt  Timestamp (in milliseconds since epoch) when the user was created. Must be positive.
 * @param updatedAt  Timestamp (in milliseconds since epoch) when the user was last updated. Must be positive.
 *
 * @throws IllegalArgumentException if any parameter is invalid (null, blank, or non-positive where not allowed).
 */
public record User(
    String id,
    String username,
    String email,
    HashedPassword password,
    long createdAt,
    long updatedAt
) {
    public User {
        if (id == null || id.isBlank()) {
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
        if (createdAt <= 0) {
            throw new IllegalArgumentException("Created at timestamp must be positive");
        }
        if (updatedAt <= 0) {
            throw new IllegalArgumentException("Updated at timestamp must be positive");
        }
    }
}
