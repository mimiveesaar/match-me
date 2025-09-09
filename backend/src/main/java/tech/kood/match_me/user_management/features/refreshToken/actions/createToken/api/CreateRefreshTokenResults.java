package tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;

@ApplicationLayer
@QueryModel
public sealed interface CreateRefreshTokenResults
        permits CreateRefreshTokenResults.Success, CreateRefreshTokenResults.UserNotFound,
        CreateRefreshTokenResults.InvalidRequest,
        CreateRefreshTokenResults.SystemError {

    record Success(@NotNull @Valid @JsonProperty("refresh_token") RefreshTokenDTO refreshToken) implements CreateRefreshTokenResults {
    }


    record InvalidRequest(@NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error) implements CreateRefreshTokenResults {
    }

    record UserNotFound(@NotEmpty @Valid @JsonProperty("user_id") UserIdDTO userId) implements CreateRefreshTokenResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message) implements CreateRefreshTokenResults {
    }
}
