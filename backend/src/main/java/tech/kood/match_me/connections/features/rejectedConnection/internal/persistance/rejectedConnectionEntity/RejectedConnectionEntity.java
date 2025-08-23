package tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity;

import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.InfrastructureLayer;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@InfrastructureLayer
public final class RejectedConnectionEntity {

    @NotNull
    private final UUID id;

    @NotNull
    private final UUID rejectedByUserId;

    @NotNull
    private final UUID rejectedUserId;

    @NotNull
    private final RejectedConnectionReason reason;

    @NotNull
    private final Instant createdAt;

    public UUID getId() {
        return id;
    }

    public UUID getRejectedByUserId() {
        return rejectedByUserId;
    }

    public UUID getRejectedUserId() {
        return rejectedUserId;
    }

    public RejectedConnectionReason getReason() {
        return reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    RejectedConnectionEntity(UUID id, UUID rejectedByUserId, UUID rejectedUserId,
            RejectedConnectionReason reason, Instant createdAt) {
        this.id = id;
        this.rejectedByUserId = rejectedByUserId;
        this.rejectedUserId = rejectedUserId;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        RejectedConnectionEntity that = (RejectedConnectionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RejectedConnectionEntity{" + "id=" + id + ", rejectedByUserId=" + rejectedByUserId
                + ", rejectedUserId=" + rejectedUserId + ", reason=" + reason + ", createdAt="
                + createdAt + '}';
    }
}
