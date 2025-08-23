package tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;

@ApplicationLayer
@QueryModel
public sealed interface CreateRefreshTokenResults
        permits CreateRefreshTokenResults.Success, CreateRefreshTokenResults.UserNotFound,
        CreateRefreshTokenResults.InvalidRequest,
        CreateRefreshTokenResults.SystemError {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
                   @NotNull @Valid @JsonProperty("refresh_token") RefreshTokenDTO refreshToken,
                   @Nullable @JsonProperty("tracing_id") String tracingId) implements CreateRefreshTokenResults {
    }


    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error,
                          @Nullable @JsonProperty("tracing_id") String tracingId) implements CreateRefreshTokenResults {
    }

    record UserNotFound(@NotNull @JsonProperty("request_id") UUID requestId,
                        @NotEmpty @Valid @JsonProperty("user_id") UserIdDTO userId,
                        @Nullable @JsonProperty("tracing_id") String tracingId) implements CreateRefreshTokenResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty @JsonProperty("message") String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements CreateRefreshTokenResults {
    }
}
