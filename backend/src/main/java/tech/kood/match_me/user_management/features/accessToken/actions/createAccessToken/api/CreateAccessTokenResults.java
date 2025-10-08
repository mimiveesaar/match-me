package tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;


@ApplicationLayer
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateAccessTokenResults.Success.class, name = "SUCCESS"),
        @JsonSubTypes.Type(value = CreateAccessTokenResults.InvalidRequest.class, name = "INVALID_REQUEST"),
        @JsonSubTypes.Type(value = CreateAccessTokenResults.InvalidToken.class, name = "INVALID_TOKEN"),
        @JsonSubTypes.Type(value = CreateAccessTokenResults.SystemError.class, name = "SYSTEM_ERROR")
})
public sealed interface CreateAccessTokenResults
        permits CreateAccessTokenResults.Success,
        CreateAccessTokenResults.InvalidToken,
        CreateAccessTokenResults.InvalidRequest,
        CreateAccessTokenResults.SystemError {


    record Success(@NotEmpty @JsonProperty("jwt") String jwt) implements CreateAccessTokenResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("data") InvalidInputErrorDTO error) implements CreateAccessTokenResults {
    }

    record InvalidToken() implements CreateAccessTokenResults {
    }

    record SystemError(@NotEmpty String message) implements CreateAccessTokenResults {
    }
}