package tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

public record GetOutgoingConnectionsRequest(
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId
) {
    public GetOutgoingConnectionsRequest withUserId(UserIdDTO userId) {
        return new GetOutgoingConnectionsRequest(userId);
    }
}
