package tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;

import java.util.List;
import java.util.UUID;

public sealed interface GetIncomingConnectionsResults permits
        GetIncomingConnectionsResults.Success,
        GetIncomingConnectionsResults.InvalidRequest,
        GetIncomingConnectionsResults.SystemError {
    record Success(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("incoming_requests") List<PendingConnectionDTO> incomingRequests,
            @Nullable @JsonProperty("tracing_id") String tracingId) implements GetIncomingConnectionsResults {
    }

    record InvalidRequest(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
            @Nullable @JsonProperty("tracing_id") String tracingId
    ) implements GetIncomingConnectionsResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements GetIncomingConnectionsResults {
    }
}