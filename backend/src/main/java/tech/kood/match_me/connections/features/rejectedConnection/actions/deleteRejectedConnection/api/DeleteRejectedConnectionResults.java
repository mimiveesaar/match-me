package tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

import java.util.UUID;

public sealed interface DeleteRejectedConnectionResults
        permits DeleteRejectedConnectionResults.Success, DeleteRejectedConnectionResults.NotFound,
        DeleteRejectedConnectionResults.AlreadyDeleted,
        DeleteRejectedConnectionResults.InvalidRequest,
        DeleteRejectedConnectionResults.SystemError {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
                   @Nullable @JsonProperty("tracing_id") String tracingId)
            implements DeleteRejectedConnectionResults {
    }

    record NotFound(@NotNull @JsonProperty("request_id") UUID requestId,
                    @Nullable @JsonProperty("tracing_id") String tracingId)
            implements DeleteRejectedConnectionResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
                          @Nullable @JsonProperty("tracing_id") String tracingId)
            implements DeleteRejectedConnectionResults {
    }

    record AlreadyDeleted(@NotNull @JsonProperty("request_id") UUID requestId,
                          @Nullable @JsonProperty("tracing_id") String tracingId)
            implements DeleteRejectedConnectionResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty String message, @Nullable @JsonProperty("tracing_id") String tracingId)
            implements DeleteRejectedConnectionResults {
    }
}
