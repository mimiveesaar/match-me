package tech.kood.match_me.user_management.internal.entities;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a user entity in the system.
 *
 * @param id            Unique identifier for the user.
 * @param email         Email address of the user.
 * @param username      Username chosen by the user.
 * @param hash Hashed password of the user.
 * @param salt Salt used for hashing the user's password.
 * @param createdAt    Timestamp when the user was created.
 * @param updatedAt    Timestamp when the user was last updated.
 */
public record UserEntity(
    UUID id,
    String email,
    String username,
    String hash,
    String salt,
    Instant createdAt,
    Instant updatedAt
) {

    public UserEntity {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (hash == null || hash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be null or blank");
        }
        if (salt == null || salt.isBlank()) {
            throw new IllegalArgumentException("Password salt cannot be null or blank");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at timestamp cannot be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at timestamp cannot be null");
        }
    }

    public UserEntity withEmail(String email) {
        return new UserEntity(id, email, username, hash, salt, createdAt, updatedAt);
    }
    public UserEntity withUsername(String username) {
        return new UserEntity(id, email, username, hash, salt, createdAt, updatedAt);
    }
    public UserEntity withHash(String hash) {
        return new UserEntity(id, email, username, hash, salt, createdAt, updatedAt);
    }
    public UserEntity withSalt(String salt) {
        return new UserEntity(id, email, username, hash, salt, createdAt, updatedAt);
    }
    public UserEntity withCreatedAt(Instant createdAt) {
        return new UserEntity(id, email, username, hash, salt, createdAt, updatedAt);
    }
    public UserEntity withUpdatedAt(Instant updatedAt) {
        return new UserEntity(id, email, username, hash, salt, createdAt, updatedAt);
    }
}