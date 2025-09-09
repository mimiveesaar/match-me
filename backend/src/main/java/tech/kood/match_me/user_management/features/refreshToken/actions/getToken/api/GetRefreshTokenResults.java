package tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;

@QueryModel
public sealed interface GetRefreshTokenResults
        permits GetRefreshTokenResults.Success, GetRefreshTokenResults.InvalidSecret,
        GetRefreshTokenResults.InvalidRequest,
        GetRefreshTokenResults.SystemError {

    record Success(@NotNull @Valid @JsonProperty("refresh_token") RefreshTokenDTO refreshToken) implements GetRefreshTokenResults {
    }

    record InvalidRequest(@NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error) implements GetRefreshTokenResults {
    }

    record InvalidSecret() implements GetRefreshTokenResults {

    }

    record SystemError(@NotEmpty @JsonProperty("message") String message) implements GetRefreshTokenResults {

    }
}