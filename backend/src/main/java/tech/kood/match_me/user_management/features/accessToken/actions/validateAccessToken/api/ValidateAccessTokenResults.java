package tech.kood.match_me.user_management.features.accessToken.actions.validateAccessToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.domain.api.AccessTokenDTO;


@ApplicationLayer
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValidateAccessTokenResults.Success.class, name = "SUCCESS"),
        @JsonSubTypes.Type(value = ValidateAccessTokenResults.InvalidRequest.class, name = "INVALID_REQUEST"),
        @JsonSubTypes.Type(value = ValidateAccessTokenResults.InvalidToken.class, name = "INVALID_TOKEN"),
        @JsonSubTypes.Type(value = ValidateAccessTokenResults.SystemError.class, name = "SYSTEM_ERROR")
})

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
