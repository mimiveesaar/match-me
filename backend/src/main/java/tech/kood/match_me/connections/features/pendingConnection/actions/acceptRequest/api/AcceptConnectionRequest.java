package tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

@Command
public record AcceptConnectionRequest(
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
        @NotNull @Valid @JsonProperty("accepted_by_user") UserIdDTO acceptedByUser) {
    public AcceptConnectionRequest withConnectionId(ConnectionIdDTO connectionIdDTO) {
        return new AcceptConnectionRequest(connectionIdDTO, acceptedByUser);
    }

    public AcceptConnectionRequest withAcceptedByUser(UserIdDTO acceptedByUser) {
        return new AcceptConnectionRequest(connectionIdDTO, acceptedByUser);
    }
}
