package tech.kood.match_me.connections.features.pendingConnection.domain.internal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.ddd.types.AggregateRoot;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionId;
import tech.kood.match_me.common.domain.internal.userId.UserId;

import java.time.Instant;
import java.util.Objects;

public class PendingConnection implements AggregateRoot<PendingConnection, ConnectionId> {

    @Valid
    @NotNull
    private final ConnectionId connectionId;

    @Valid
    @NotNull
    private final UserId senderId;

    @Valid
    @NotNull
    private final UserId targetId;

    @Valid
    @NotNull
    private final Instant createdAt;

    public UserId getSenderId() {
        return senderId;
    }

    public UserId getTargetId() {
        return targetId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public ConnectionId getId() {
        return null;
    }

    PendingConnection(ConnectionId connectionId, UserId senderId, UserId targetId, Instant createdAt) {
        this.connectionId = connectionId;
        this.senderId = senderId;
        this.targetId = targetId;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PendingConnection that = (PendingConnection) o;
        return Objects.equals(connectionId, that.connectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(connectionId);
    }

    @Override
    public String toString() {
        return "PendingConnection{" +
                "connectionId=" + connectionId +
                ", senderId=" + senderId +
                ", targetId=" + targetId +
                ", sentAt=" + createdAt +
                '}';
    }
}