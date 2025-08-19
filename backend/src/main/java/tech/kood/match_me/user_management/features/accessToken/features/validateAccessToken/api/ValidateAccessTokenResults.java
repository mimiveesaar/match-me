package tech.kood.match_me.user_management.features.accessToken.features.validateAccessToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.features.accessToken.domain.api.AccessTokenDTO;

public sealed interface ValidateAccessTokenResults permits
        ValidateAccessTokenResults.Success,
        ValidateAccessTokenResults.InvalidToken,
        ValidateAccessTokenResults.InvalidRequest,
        ValidateAccessTokenResults.SystemError {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
                   @NotNull @Valid @JsonProperty("access_token") AccessTokenDTO accessToken,
                   @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId,
                   @JsonProperty("tracing_id") @Nullable String tracingId) implements ValidateAccessTokenResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error,
                          @JsonProperty("tracing_id") @Nullable String tracingId) implements ValidateAccessTokenResults {
    }

    record InvalidToken(@NotNull @JsonProperty("request_id") UUID requestId,
                        @Nullable @JsonProperty("tracing_id") String tracingId) implements ValidateAccessTokenResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty @JsonProperty("message") String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements ValidateAccessTokenResults {
    }
}
