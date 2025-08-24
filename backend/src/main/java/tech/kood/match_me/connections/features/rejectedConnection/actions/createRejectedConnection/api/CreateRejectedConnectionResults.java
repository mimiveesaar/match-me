package tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.common.api.ConnectionId;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

public sealed interface CreateRejectedConnectionResults
        permits CreateRejectedConnectionResults.Success,
        CreateRejectedConnectionResults.AlreadyExists, CreateRejectedConnectionResults.SystemError,
        CreateRejectedConnectionResults.InvalidRequest {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("connection_id") ConnectionId connectionId,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements CreateRejectedConnectionResults {
    }

    record AlreadyExists(@NotNull @JsonProperty("request_id") UUID requestId,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements CreateRejectedConnectionResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements CreateRejectedConnectionResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotEmpty @JsonProperty("message") String message,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements CreateRejectedConnectionResults {
    }
}
