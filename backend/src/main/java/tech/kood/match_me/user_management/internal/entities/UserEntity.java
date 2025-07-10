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
 * @param password_hash Hashed password of the user.
 * @param password_salt Salt used for hashing the user's password.
 * @param created_at    Timestamp when the user was created.
 * @param updated_at    Timestamp when the user was last updated.
 */
@Builder
public record UserEntity(
    UUID id,
    String email,
    String username,
    String password_hash,
    String password_salt,
    Instant created_at,
    Instant updated_at
) {}