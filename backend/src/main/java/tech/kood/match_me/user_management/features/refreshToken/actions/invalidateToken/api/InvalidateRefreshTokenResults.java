package tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotEmpty;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;


@ApplicationLayer
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        {
            @JsonSubTypes.Type(value = InvalidateRefreshTokenResults.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = InvalidateRefreshTokenResults.TokenNotFound.class, name = "TOKEN_NOT_FOUND"),
            @JsonSubTypes.Type(value = InvalidateRefreshTokenResults.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = InvalidateRefreshTokenResults.SystemError.class, name = "SYSTEM_ERROR")
        }
)
public sealed interface InvalidateRefreshTokenResults permits
        InvalidateRefreshTokenResults.Success,
        InvalidateRefreshTokenResults.TokenNotFound,
        InvalidateRefreshTokenResults.InvalidRequest,
        InvalidateRefreshTokenResults.SystemError {

    record Success() implements InvalidateRefreshTokenResults {
    }

    record TokenNotFound() implements InvalidateRefreshTokenResults {
    }

    record InvalidRequest(@NotEmpty @JsonProperty("error") InvalidInputErrorDTO error) implements InvalidateRefreshTokenResults {
    }

    record SystemError(@NotEmpty @JsonProperty("message") String message) implements InvalidateRefreshTokenResults {
    }
}