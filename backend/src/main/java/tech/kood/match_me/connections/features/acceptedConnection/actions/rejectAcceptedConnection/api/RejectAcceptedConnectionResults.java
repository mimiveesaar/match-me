package tech.kood.match_me.connections.features.acceptedConnection.actions.rejectAcceptedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

import java.util.UUID;

public sealed interface RejectAcceptedConnectionResults
        permits RejectAcceptedConnectionResults.Success, RejectAcceptedConnectionResults.NotFound,
        RejectAcceptedConnectionResults.AlreadyRejected,
        RejectAcceptedConnectionResults.InvalidRequest,
        RejectAcceptedConnectionResults.SystemError {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements RejectAcceptedConnectionResults {
    }

    record NotFound(@NotNull @JsonProperty("request_id") UUID requestId,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements RejectAcceptedConnectionResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements RejectAcceptedConnectionResults {
    }

    record AlreadyRejected(@NotNull @JsonProperty("request_id") UUID requestId,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements RejectAcceptedConnectionResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotEmpty String message, @Nullable @JsonProperty("tracing_id") String tracingId)
            implements RejectAcceptedConnectionResults {
    }
}
