package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

@Command
public record DeclineConnectionRequest(
        @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
        @NotNull @Valid @JsonProperty("declined_by_user") UserIdDTO declinedByUser
) {
    public DeclineConnectionRequest withConnectionId(ConnectionIdDTO connectionIdDTO) {
        return new DeclineConnectionRequest(connectionIdDTO, declinedByUser);
    }
    public DeclineConnectionRequest withDeclinedByUser(UserIdDTO declinedByUser) {
        return new DeclineConnectionRequest(connectionIdDTO, declinedByUser);
    }
}