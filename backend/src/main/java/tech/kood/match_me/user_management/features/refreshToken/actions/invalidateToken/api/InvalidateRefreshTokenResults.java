package tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;


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

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
                   @Nullable @JsonProperty("tracing_id") String tracingId) implements InvalidateRefreshTokenResults {
    }

    record TokenNotFound(@NotNull @JsonProperty("request_id") UUID requestId,
                         @Nullable @JsonProperty("tracing_id") String tracingId) implements InvalidateRefreshTokenResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotEmpty @JsonProperty("error") InvalidInputErrorDTO error,
                          @Nullable @JsonProperty("tracing_id") String tracingId) implements InvalidateRefreshTokenResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty @JsonProperty("message") String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements InvalidateRefreshTokenResults {
    }
}