package tech.kood.match_me.user_management.entities;

import java.time.Instant;
import java.util.UUID;

public record UserEntity(
    UUID id,
    String email,
    String username,
    String password_hash,
    String password_salt,
    Instant created_at,
    Instant updated_at
) {}