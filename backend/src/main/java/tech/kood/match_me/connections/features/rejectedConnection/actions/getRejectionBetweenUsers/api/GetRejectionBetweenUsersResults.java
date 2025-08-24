package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionDTO;

public sealed interface GetRejectionBetweenUsersResults
        permits GetRejectionBetweenUsersResults.Success, GetRejectionBetweenUsersResults.NotFound,
        GetRejectionBetweenUsersResults.SystemError,
        GetRejectionBetweenUsersResults.InvalidRequest {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @Valid @JsonProperty("rejection") RejectedConnectionDTO rejection,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements GetRejectionBetweenUsersResults {
    }

    record NotFound(@NotNull @JsonProperty("request_id") UUID requestId,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements GetRejectionBetweenUsersResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements GetRejectionBetweenUsersResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotEmpty @JsonProperty("message") String message,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements GetRejectionBetweenUsersResults {
    }
}
