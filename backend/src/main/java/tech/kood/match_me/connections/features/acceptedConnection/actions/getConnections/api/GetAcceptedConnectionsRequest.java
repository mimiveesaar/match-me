package tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

public record GetAcceptedConnectionsRequest(
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId
) {
    public GetAcceptedConnectionsRequest withUserId(UserIdDTO userId) {
        return new GetAcceptedConnectionsRequest(userId);
    }
}
