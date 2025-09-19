package tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

@Command
public record RejectAcceptedConnectionRequest(@NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionId,
                                              @NotNull @Valid @JsonProperty("user_id") UserIdDTO rejectedBy) {

    public RejectAcceptedConnectionRequest withConnectionId(ConnectionIdDTO connectionIdDTO) {
        return new RejectAcceptedConnectionRequest(connectionIdDTO, rejectedBy);
    }

    public RejectAcceptedConnectionRequest withRejectedBy(UserIdDTO rejectedBy) {
        return new RejectAcceptedConnectionRequest(connectionId, rejectedBy);
    }
}
