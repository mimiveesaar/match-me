package tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;

import java.util.List;
import java.util.UUID;

public sealed interface GetOutgoingConnectionsResults permits
        GetOutgoingConnectionsResults.Success,
        GetOutgoingConnectionsResults.InvalidRequest,
        GetOutgoingConnectionsResults.SystemError {
    record Success(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("outgoing_requests") List<PendingConnectionDTO> outgoingRequests,
            @Nullable @JsonProperty("tracing_id") String tracingId) implements GetOutgoingConnectionsResults {
    }

    record InvalidRequest(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
            @Nullable @JsonProperty("tracing_id") String tracingId
    ) implements GetOutgoingConnectionsResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements GetOutgoingConnectionsResults {
    }
}
