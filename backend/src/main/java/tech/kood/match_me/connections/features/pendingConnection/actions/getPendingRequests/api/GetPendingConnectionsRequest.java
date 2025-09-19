package tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

public record GetPendingConnectionsRequest(
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId
) {
    public GetPendingConnectionsRequest withUserId(UserIdDTO userId) {
        return new GetPendingConnectionsRequest(userId);
    }
}
