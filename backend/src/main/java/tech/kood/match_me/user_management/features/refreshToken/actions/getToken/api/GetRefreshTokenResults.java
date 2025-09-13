package tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;

import java.util.UUID;

@QueryModel
public sealed interface GetRefreshTokenResults
        permits GetRefreshTokenResults.Success, GetRefreshTokenResults.InvalidSecret,
        GetRefreshTokenResults.InvalidRequest,
        GetRefreshTokenResults.SystemError {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
                   @NotNull @Valid @JsonProperty("refresh_token") RefreshTokenDTO refreshToken,
                   @Nullable @JsonProperty("tracing_id") String tracingId) implements GetRefreshTokenResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error,
                          @Nullable @JsonProperty("tracing_id") String tracingId) implements GetRefreshTokenResults {
    }

    record InvalidSecret(@NotNull @JsonProperty("request_id") UUID requestId,
                         @Nullable @JsonProperty("tracing_id") String tracingId) implements GetRefreshTokenResults {

    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty @JsonProperty("message") String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements GetRefreshTokenResults {

    }
}