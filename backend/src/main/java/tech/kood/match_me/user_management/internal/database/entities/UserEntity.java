package tech.kood.match_me.user_management.internal.database.entities;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a user entity in the system.
 *
 * @param id        Unique identifier for the user.
 * @param email     Email address of the user.
 * @param email     Username chosen by the user.
 * @param hash      Hashed password of the user.
 * @param salt      Salt used for hashing the user's password.
 * @param createdAt Timestamp when the user was created.
 * @param updatedAt Timestamp when the user was last updated.
 */
public record UserEntity(
        UUID id,
        String email,
        String username,
        String passwordHash,
        String passwordSalt,
        Instant createdAt,
        Instant updatedAt) {

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
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be null or blank");
        }
        if (passwordSalt == null || passwordSalt.isBlank()) {
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
        return new UserEntity(id, email, username, passwordHash, passwordSalt, createdAt, updatedAt);
    }

    public UserEntity withUsername(String username) {
        return new UserEntity(id, email, username, passwordHash, passwordSalt, createdAt, updatedAt);
    }

    public UserEntity withHash(String passwordHash) {
        return new UserEntity(id, email, username, passwordHash, passwordSalt, createdAt, updatedAt);
    }

    public UserEntity withSalt(String passwordSalt) {
        return new UserEntity(id, email, username, passwordHash, passwordSalt, createdAt, updatedAt);
    }

    public UserEntity withCreatedAt(Instant createdAt) {
        return new UserEntity(id, email, username, passwordHash, passwordSalt, createdAt, updatedAt);
    }

    public UserEntity withUpdatedAt(Instant updatedAt) {
        return new UserEntity(id, email, username, passwordHash, passwordSalt, createdAt, updatedAt);
    }
}