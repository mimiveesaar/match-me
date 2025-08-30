package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

import java.util.UUID;

public sealed interface DeclineConnectionResults permits
        DeclineConnectionResults.Success, DeclineConnectionResults.NotFound, DeclineConnectionResults.InvalidRequest,
        DeclineConnectionResults.AlreadyDeclined, DeclineConnectionResults.SystemError {


    record Success(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @Nullable @JsonProperty("tracing_id") String tracingId) implements DeclineConnectionResults {
    }

    record InvalidRequest(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
            @Nullable @JsonProperty("tracing_id") String tracingId) implements DeclineConnectionResults {
    }

    record NotFound(
            @NotNull @JsonProperty("request_id") UUID requestId,
            @Nullable @JsonProperty("tracing_id") String tracingId) implements DeclineConnectionResults {
    }

    record AlreadyDeclined(@NotNull @JsonProperty("request_id") UUID requestId,
                           @Nullable @JsonProperty("tracing_id") String tracingId) implements DeclineConnectionResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements DeclineConnectionResults {
    }
}
