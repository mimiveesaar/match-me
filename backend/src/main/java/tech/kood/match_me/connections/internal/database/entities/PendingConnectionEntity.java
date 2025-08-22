package tech.kood.match_me.connections.internal.database.entities;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a pending connection entity in the system.
 *
 * @param id Unique identifier for the pending connection.
 * @param senderUserId UUID of the userId who sent the connection request.
 * @param targetUserId UUID of the userId who received the connection request.
 * @param createdAt Timestamp when the pending connection was created.
 */
public record PendingConnectionEntity(UUID id, UUID senderUserId, UUID targetUserId,
        Instant createdAt) {

    public PendingConnectionEntity {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (senderUserId == null) {
            throw new IllegalArgumentException("Sender userId ID cannot be null");
        }
        if (targetUserId == null) {
            throw new IllegalArgumentException("Target userId ID cannot be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at timestamp cannot be null");
        }
        if (senderUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("Sender and target userId IDs cannot be the same");
        }
    }

    public PendingConnectionEntity withId(UUID id) {
        return new PendingConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }

    public PendingConnectionEntity withSenderUserId(UUID senderUserId) {
        return new PendingConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }

    public PendingConnectionEntity withTargetUserId(UUID targetUserId) {
        return new PendingConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }

    public PendingConnectionEntity withCreatedAt(Instant createdAt) {
        return new PendingConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }
}
