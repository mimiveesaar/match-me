package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

public sealed interface ConnectionRequestResults
        permits ConnectionRequestResults.Success, ConnectionRequestResults.AlreadyExists,
        ConnectionRequestResults.SystemError, ConnectionRequestResults.InvalidRequest {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionId,
                          @Nullable @JsonProperty("tracing_id") String tracingId)
            implements ConnectionRequestResults {
    }

    record AlreadyExists(@NotNull @JsonProperty("request_id") UUID requestId,
                                @Nullable @JsonProperty("tracing_id") String tracingId) implements ConnectionRequestResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId, @NotNull @JsonProperty("error") InvalidInputErrorDTO error, @Nullable @JsonProperty("tracing_id") String tracingId)
            implements ConnectionRequestResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId, @NotEmpty @JsonProperty("message") String message, @Nullable @JsonProperty("tracing_id") String tracingId)
            implements ConnectionRequestResults {
    }
}
