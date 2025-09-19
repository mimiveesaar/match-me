package tech.kood.match_me.connections.features.rejectedConnection.domain.internal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.ddd.types.AggregateRoot;
import tech.kood.match_me.common.domain.internal.userId.UserId;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionId;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.validation.ValidRejectedConnection;

import java.time.Instant;
import java.util.Objects;

@ValidRejectedConnection
public class RejectedConnection implements AggregateRoot<RejectedConnection, ConnectionId> {

    @Valid
    @NotNull
    private final ConnectionId connectionId;

    @Valid
    @NotNull
    private final UserId rejectedByUser;

    @Valid
    @NotNull
    private final UserId rejectedUser;

    @Valid
    @NotNull
    private final RejectedConnectionReason reason;

    @Valid
    @NotNull
    private final Instant createdAt;

    public UserId getRejectedByUser() {
        return rejectedByUser;
    }

    public UserId getRejectedUser() {
        return rejectedUser;
    }

    public RejectedConnectionReason getReason() {
        return reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public ConnectionId getId() {
        return connectionId;
    }

    RejectedConnection(ConnectionId connectionId, UserId rejectedByUser, UserId rejectedUser,
            RejectedConnectionReason reason, Instant createdAt) {
        this.connectionId = connectionId;
        this.rejectedByUser = rejectedByUser;
        this.rejectedUser = rejectedUser;
        this.reason = reason;
        this.createdAt = createdAt;

        //User should not be able to reject themselves.
        if (this.rejectedByUser.equals(this.rejectedUser)) {
            throw new IllegalArgumentException("Rejected user cannot be the same as rejected by user");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        RejectedConnection that = (RejectedConnection) o;
        return Objects.equals(connectionId, that.connectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(connectionId);
    }

    @Override
    public String toString() {
        return "RejectedConnection{" + "connectionId=" + connectionId + ", rejectedByUser="
                + rejectedByUser + ", rejectedUser=" + rejectedUser + ", reason=" + reason
                + ", createdAt=" + createdAt + '}';
    }
}
