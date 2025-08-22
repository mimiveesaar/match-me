package tech.kood.match_me.user_management.features.user.features.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;

import java.util.UUID;

public sealed interface GetUserByEmailResults
        permits
        GetUserByEmailResults.Success,
        GetUserByEmailResults.UserNotFound,
        GetUserByEmailResults.InvalidRequest,
        GetUserByEmailResults.SystemError  {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId, @NotNull @Valid @JsonProperty("userId") UserDTO user, @Nullable @JsonProperty("tracing_id") String tracingId) implements GetUserByEmailResults {}
    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId, @NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error, @Nullable @JsonProperty("tracing_id") String tracingId) implements GetUserByEmailResults {}
    record UserNotFound(@NotNull @JsonProperty("request_id") UUID requestId, @NotNull @Valid @JsonProperty("email") EmailDTO email, @Nullable @JsonProperty("tracing_id") String tracingId) implements GetUserByEmailResults {}
    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId, @NotEmpty @JsonProperty("message") String message, @Nullable @JsonProperty("tracing_id") String tracingId) implements GetUserByEmailResults {}
}
