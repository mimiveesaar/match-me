package tech.kood.match_me.user_management.features.accessToken.features.createAccessToken;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;

public sealed interface CreateAccessTokenResults
        permits CreateAccessTokenResults.Success,
        CreateAccessTokenResults.InvalidToken,
        CreateAccessTokenResults.InvalidRequest,
        CreateAccessTokenResults.SystemError {


    record Success(@NotNull @JsonProperty("request_id") UUID requestId, @NotEmpty @JsonProperty("jwt") String jwt,
                   @Nullable @JsonProperty("tracing_id") String tracingId) implements CreateAccessTokenResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotNull @JsonProperty("error") InvalidInputErrorDTO error,
                          @Nullable String tracingId) implements CreateAccessTokenResults {
    }

    record InvalidToken(@NotNull @JsonProperty("request_id") UUID requestId,
                        @Nullable String tracingId) implements CreateAccessTokenResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId, @NotEmpty String message,
                       @Nullable String tracingId) implements CreateAccessTokenResults {
    }
}