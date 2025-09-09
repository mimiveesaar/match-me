package tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.util.UUID;

public record GetAcceptedConnectionsRequest(
        @NotNull @JsonProperty("request_id") UUID requestId,
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId,
        @Nullable @JsonProperty("tracing_id") String tracingId
) {
    public GetAcceptedConnectionsRequest(UserIdDTO userId, @Nullable String tracingId) {
        this(UUID.randomUUID(), userId, tracingId);
    }

    public GetAcceptedConnectionsRequest withRequestId(UUID requestId) {
        return new GetAcceptedConnectionsRequest(requestId, userId, tracingId);
    }

    public GetAcceptedConnectionsRequest withUserId(UserIdDTO userId) {
        return new GetAcceptedConnectionsRequest(requestId, userId, tracingId);
    }

    public GetAcceptedConnectionsRequest withTracingId(String tracingId) {
        return new GetAcceptedConnectionsRequest(requestId, userId, tracingId);
    }
}
