package tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity;

import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.InfrastructureLayer;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@InfrastructureLayer
public final class AcceptedConnectionEntity {

    @NotNull
    private final UUID id;

    @NotNull
    private final UUID acceptedByUserId;

    @NotNull
    private final UUID acceptedUserId;

    @NotNull
    private final Instant createdAt;

    public UUID getId() {
        return id;
    }

    public UUID getAcceptedByUserId() {
        return acceptedByUserId;
    }

    public UUID getAcceptedUserId() {
        return acceptedUserId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    AcceptedConnectionEntity(UUID id, UUID acceptedByUserId, UUID acceptedUserId,
            Instant createdAt) {
        this.id = id;
        this.acceptedByUserId = acceptedByUserId;
        this.acceptedUserId = acceptedUserId;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        AcceptedConnectionEntity that = (AcceptedConnectionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AcceptedConnectionEntity{" + "id=" + id + ", acceptedByUserId=" + acceptedByUserId
                + ", acceptedUserId=" + acceptedUserId + ", createdAt=" + createdAt + '}';
    }
}
