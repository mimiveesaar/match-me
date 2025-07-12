package tech.kood.match_me.user_management.internal.entities;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;

/**
 * Represents a user entity in the system.
 *
 * @param id            Unique identifier for the user.
 * @param email         Email address of the user.
 * @param username      Username chosen by the user.
 * @param passwordHash Hashed password of the user.
 * @param passwordSalt Salt used for hashing the user's password.
 * @param createdAt    Timestamp when the user was created.
 * @param updatedAt    Timestamp when the user was last updated.
 */
@Builder
public record UserEntity(
    UUID id,
    String email,
    String username,
    String passwordHash,
    String passwordSalt,
    Instant createdAt,
    Instant updatedAt
) {}