package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionDTO;

public sealed interface GetRejectedUsersByUserResults permits GetRejectedUsersByUserResults.Success,
        GetRejectedUsersByUserResults.SystemError, GetRejectedUsersByUserResults.InvalidRequest {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("rejections") List<@Valid RejectedConnectionDTO> rejections,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements GetRejectedUsersByUserResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements GetRejectedUsersByUserResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
            @NotEmpty @JsonProperty("message") String message,
            @Nullable @JsonProperty("tracing_id") String tracingId)
            implements GetRejectedUsersByUserResults {
    }
}
