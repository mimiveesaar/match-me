package tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

@Command
public record CreateAcceptedConnectionRequest(
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionId,
        @NotNull @Valid @JsonProperty("accepted_by_user") UserIdDTO acceptedByUser,
        @NotNull @Valid @JsonProperty("accepted_user") UserIdDTO acceptedUser) {

    public CreateAcceptedConnectionRequest withConnectionId(ConnectionIdDTO connectionId) {
        return new CreateAcceptedConnectionRequest(connectionId, acceptedByUser, acceptedUser);
    }

    public CreateAcceptedConnectionRequest withAcceptedByUser(UserIdDTO acceptedByUser) {
        return new CreateAcceptedConnectionRequest(connectionId, acceptedByUser, acceptedUser);
    }

    public CreateAcceptedConnectionRequest withAcceptedUser(UserIdDTO acceptedUser) {
        return new CreateAcceptedConnectionRequest(connectionId, acceptedByUser, acceptedUser);
    }
}
