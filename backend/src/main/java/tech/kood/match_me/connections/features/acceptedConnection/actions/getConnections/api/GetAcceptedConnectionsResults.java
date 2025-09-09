package tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.acceptedConnection.domain.api.AcceptedConnectionDTO;

import java.util.List;
import java.util.UUID;

public sealed interface GetAcceptedConnectionsResults permits
        GetAcceptedConnectionsResults.Success,
        GetAcceptedConnectionsResults.InvalidRequest,
        GetAcceptedConnectionsResults.SystemError {
    
    record Success(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("accepted_connections") List<AcceptedConnectionDTO> acceptedConnections,
            @Nullable @JsonProperty("tracing_id") String tracingId) implements GetAcceptedConnectionsResults {
    }

    record InvalidRequest(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
            @Nullable @JsonProperty("tracing_id") String tracingId
    ) implements GetAcceptedConnectionsResults {
    }

    record SystemError(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @NotEmpty @JsonProperty("message") String message,
            @Nullable @JsonProperty("tracing_id") String tracingId) implements GetAcceptedConnectionsResults {
    }
}
