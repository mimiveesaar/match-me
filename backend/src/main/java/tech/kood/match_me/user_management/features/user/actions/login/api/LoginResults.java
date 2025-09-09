package tech.kood.match_me.user_management.features.user.actions.login.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = LoginResults.Success.class, name = "SUCCESS"),
                @JsonSubTypes.Type(value = LoginResults.InvalidRequest.class, name = "INVALID_REQUEST"),
                @JsonSubTypes.Type(value = LoginResults.InvalidCredentials.class, name = "INVALID_CREDENTIALS"),
                @JsonSubTypes.Type(value = LoginResults.SystemError.class, name = "SYSTEM_ERROR")
        }
)
public sealed interface LoginResults
        permits
        LoginResults.Success,
        LoginResults.InvalidCredentials,
        LoginResults.InvalidRequest,
        LoginResults.SystemError {

    record Success(@NotNull @Valid @JsonProperty("refresh_token") RefreshTokenDTO refreshToken) implements LoginResults {
    }

    record InvalidRequest(@NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error) implements LoginResults {
    }

    record InvalidCredentials() implements LoginResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message) implements LoginResults {
    }
}
