package tech.kood.match_me.user_management.features.accessToken.actions.validateAccessToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.features.accessToken.domain.api.AccessTokenDTO;

public sealed interface ValidateAccessTokenResults permits
        ValidateAccessTokenResults.Success,
        ValidateAccessTokenResults.InvalidToken,
        ValidateAccessTokenResults.InvalidRequest,
        ValidateAccessTokenResults.SystemError {

    record Success(@NotNull @Valid @JsonProperty("access_token") AccessTokenDTO accessToken,
                   @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId) implements ValidateAccessTokenResults {
    }

    record InvalidRequest(@NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error) implements ValidateAccessTokenResults {
    }

    record InvalidToken() implements ValidateAccessTokenResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message) implements ValidateAccessTokenResults {
    }
}
