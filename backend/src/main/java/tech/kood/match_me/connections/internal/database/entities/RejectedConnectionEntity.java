package tech.kood.match_me.connections.internal.database.entities;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a rejected connection between two users. This record stores information about
 * connection requests that have been explicitly rejected.
 */
public record RejectedConnectionEntity(UUID id, UUID senderUserId, UUID targetUserId,
        UUID rejectedByUserId, Instant createdAt) {

    public RejectedConnectionEntity {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (senderUserId == null) {
            throw new IllegalArgumentException("Sender User ID cannot be null");
        }
        if (targetUserId == null) {
            throw new IllegalArgumentException("Target User ID cannot be null");
        }
        if (rejectedByUserId == null) {
            throw new IllegalArgumentException("Rejected By User ID cannot be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created At cannot be null");
        }
        if (senderUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("Sender and target userId IDs cannot be the same");
        }
        if (senderUserId.equals(rejectedByUserId)) {
            throw new IllegalArgumentException(
                    "Sender and rejected by userId IDs cannot be the same");
        }
        if (targetUserId.equals(rejectedByUserId)) {
            throw new IllegalArgumentException(
                    "Target and rejected by userId IDs cannot be the same");
        }
    }

    public RejectedConnectionEntity withId(UUID id) {
        return new RejectedConnectionEntity(id, senderUserId, targetUserId, rejectedByUserId,
                createdAt);
    }

    public RejectedConnectionEntity withSenderUserId(UUID senderUserId) {
        return new RejectedConnectionEntity(id, senderUserId, targetUserId, rejectedByUserId,
                createdAt);
    }

    public RejectedConnectionEntity withTargetUserId(UUID targetUserId) {
        return new RejectedConnectionEntity(id, senderUserId, targetUserId, rejectedByUserId,
                createdAt);
    }

    public RejectedConnectionEntity withRejectedByUserId(UUID rejectedByUserId) {
        return new RejectedConnectionEntity(id, senderUserId, targetUserId, rejectedByUserId,
                createdAt);
    }

    public RejectedConnectionEntity withCreatedAt(Instant createdAt) {
        return new RejectedConnectionEntity(id, senderUserId, targetUserId, rejectedByUserId,
                createdAt);
    }
}
