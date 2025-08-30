package tech.kood.match_me.connections.features.acceptedConnection.domain.internal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.ddd.types.AggregateRoot;
import tech.kood.match_me.connections.common.domain.connectionId.ConnectionId;
import tech.kood.match_me.common.domain.internal.userId.UserId;

import java.time.Instant;
import java.util.Objects;

public class AcceptedConnection implements AggregateRoot<AcceptedConnection, ConnectionId> {

    @Valid
    @NotNull
    private final ConnectionId connectionId;

    @Valid
    @NotNull
    private final UserId acceptedByUser;

    @Valid
    @NotNull
    private final UserId acceptedUser;

    @Valid
    @NotNull
    private final Instant createdAt;

    public UserId getAcceptedByUser() {
        return acceptedByUser;
    }

    public UserId getAcceptedUser() {
        return acceptedUser;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public ConnectionId getId() {
        return connectionId;
    }

    AcceptedConnection(ConnectionId connectionId, UserId acceptedByUser, UserId acceptedUser,
            Instant createdAt) {
        this.connectionId = connectionId;
        this.acceptedByUser = acceptedByUser;
        this.acceptedUser = acceptedUser;
        this.createdAt = createdAt;

        // User should not be able to accept themselves
        if (this.acceptedByUser.equals(this.acceptedUser)) {
            throw new IllegalArgumentException(
                    "Accepted user cannot be the same as accepted by user");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        AcceptedConnection that = (AcceptedConnection) o;
        return Objects.equals(connectionId, that.connectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(connectionId);
    }

    @Override
    public String toString() {
        return "AcceptedConnection{" + "connectionId=" + connectionId + ", acceptedByUser="
                + acceptedByUser + ", acceptedUser=" + acceptedUser + ", createdAt=" + createdAt
                + '}';
    }
}
