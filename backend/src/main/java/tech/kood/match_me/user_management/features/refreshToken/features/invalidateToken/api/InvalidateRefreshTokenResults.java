package tech.kood.match_me.user_management.features.refreshToken.features.invalidateToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;

public sealed interface InvalidateRefreshTokenResults permits
        InvalidateRefreshTokenResults.Success,
        InvalidateRefreshTokenResults.TokenNotFound,
        InvalidateRefreshTokenResults.InvalidRequest,
        InvalidateRefreshTokenResults.SystemError {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
                   @Nullable @JsonProperty("tracing_id") String tracingId) implements InvalidateRefreshTokenResults {
    }

    record TokenNotFound(@NotNull @JsonProperty("request_id") UUID requestId,
                         @Nullable @JsonProperty("tracing_id") String tracingId) implements InvalidateRefreshTokenResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotEmpty @JsonProperty("error") InvalidInputErrorDTO error,
                          @Nullable @JsonProperty("tracing_id") String tracingId) implements InvalidateRefreshTokenResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty @JsonProperty("message") String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements InvalidateRefreshTokenResults {
    }
}