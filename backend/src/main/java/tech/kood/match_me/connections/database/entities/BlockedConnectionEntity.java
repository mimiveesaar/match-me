package tech.kood.match_me.connections.database.entities;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a blocked connection between two users. This record stores information about
 * users blocking each other.
 */
public record BlockedConnectionEntity(UUID id, UUID blockerUserId, UUID blockedUserId,
        Instant createdAt) {

    public BlockedConnectionEntity {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (blockerUserId == null) {
            throw new IllegalArgumentException("Blocker User ID cannot be null");
        }
        if (blockedUserId == null) {
            throw new IllegalArgumentException("Blocked User ID cannot be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created At cannot be null");
        }
        if (blockerUserId.equals(blockedUserId)) {
            throw new IllegalArgumentException("Blocker and blocked user IDs cannot be the same");
        }
    }

    public BlockedConnectionEntity withId(UUID id) {
        return new BlockedConnectionEntity(id, blockerUserId, blockedUserId, createdAt);
    }

    public BlockedConnectionEntity withBlockerUserId(UUID blockerUserId) {
        return new BlockedConnectionEntity(id, blockerUserId, blockedUserId, createdAt);
    }

    public BlockedConnectionEntity withBlockedUserId(UUID blockedUserId) {
        return new BlockedConnectionEntity(id, blockerUserId, blockedUserId, createdAt);
    }

    public BlockedConnectionEntity withCreatedAt(Instant createdAt) {
        return new BlockedConnectionEntity(id, blockerUserId, blockedUserId, createdAt);
    }
}
