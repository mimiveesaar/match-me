package tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity;

import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.InfrastructureLayer;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@InfrastructureLayer
public final class PendingConnectionEntity {

    @NotNull
    private final UUID id;

    @NotNull
    private final UUID senderId;

    @NotNull
    private final UUID targetId;

    @NotNull
    private final Instant createdAt;

    public UUID getId() {
        return id;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    PendingConnectionEntity(UUID id, UUID senderId, UUID targetId, Instant createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.targetId = targetId;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PendingConnectionEntity that = (PendingConnectionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PendingConnectionEntity{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", targetId=" + targetId +
                ", createdAt=" + createdAt +
                '}';
    }
}
