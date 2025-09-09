package tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.util.UUID;

public record GetOutgoingConnectionsRequest(
        @NotNull @JsonProperty("request_id") UUID requestId,
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId,
        @Nullable @JsonProperty("tracing_id") String tracingId
) {
    public GetOutgoingConnectionsRequest withRequestId(UUID requestId) {
        return new GetOutgoingConnectionsRequest(requestId, userId, tracingId);
    }

    public GetOutgoingConnectionsRequest withUserId(UserIdDTO userId) {
        return new GetOutgoingConnectionsRequest(requestId, userId, tracingId);
    }

    public GetOutgoingConnectionsRequest withTracingId(String tracingId) {
        return new GetOutgoingConnectionsRequest(requestId, userId, tracingId);
    }
}
