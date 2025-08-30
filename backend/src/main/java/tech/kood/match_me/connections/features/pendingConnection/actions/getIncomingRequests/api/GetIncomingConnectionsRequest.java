package tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.util.UUID;

public record GetIncomingConnectionsRequest(
        @NotNull @JsonProperty("request_id") UUID requestId,
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId,
        @Nullable @JsonProperty("tracing_id") String tracingId
) {
    public GetIncomingConnectionsRequest withRequestId(UUID requestId) {
        return new GetIncomingConnectionsRequest(requestId, userId, tracingId);
    }

    public GetIncomingConnectionsRequest withUserId(UserIdDTO userId) {
        return new GetIncomingConnectionsRequest(requestId, userId, tracingId);
    }

    public GetIncomingConnectionsRequest withTracingId(String tracingId) {
        return new GetIncomingConnectionsRequest(requestId, userId, tracingId);
    }
}
