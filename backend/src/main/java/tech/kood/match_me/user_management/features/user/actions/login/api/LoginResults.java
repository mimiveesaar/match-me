package tech.kood.match_me.user_management.features.user.actions.login.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.annotation.Nullable;
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

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
                   @NotNull @Valid @JsonProperty("refresh_token") RefreshTokenDTO refreshToken,
                   @Nullable @JsonProperty("tracing_id") String tracingId) implements LoginResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error,
                          @Nullable @JsonProperty("tracing_id") String tracingId) implements LoginResults {
    }

    record InvalidCredentials(@NotNull @JsonProperty("request_id") UUID requestId,
                              @Nullable @JsonProperty("tracing_id") String tracingId) implements LoginResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty @JsonProperty("message") String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements LoginResults {
    }
}
