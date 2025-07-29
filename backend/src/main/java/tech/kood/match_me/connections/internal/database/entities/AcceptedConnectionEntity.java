package tech.kood.match_me.connections.internal.database.entities;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents an accepted connection entity in the system.
 *
 * @param id Unique identifier for the accepted connection.
 * @param senderUserId UUID of the user who sent the connection request.
 * @param targetUserId UUID of the user who accepted the connection request.
 * @param createdAt Timestamp when the accepted connection was created.
 */
public record AcceptedConnectionEntity(UUID id, UUID senderUserId, UUID targetUserId,
        Instant createdAt) {

    public AcceptedConnectionEntity {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (senderUserId == null) {
            throw new IllegalArgumentException("Sender user ID cannot be null");
        }
        if (targetUserId == null) {
            throw new IllegalArgumentException("Target user ID cannot be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at timestamp cannot be null");
        }
        if (senderUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("Sender and target user IDs cannot be the same");
        }
    }

    public AcceptedConnectionEntity withId(UUID id) {
        return new AcceptedConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }

    public AcceptedConnectionEntity withSenderUserId(UUID senderUserId) {
        return new AcceptedConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }

    public AcceptedConnectionEntity withTargetUserId(UUID targetUserId) {
        return new AcceptedConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }

    public AcceptedConnectionEntity withCreatedAt(Instant createdAt) {
        return new AcceptedConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }
}
