package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.util.UUID;

public sealed interface GetUserByIdResults permits
        GetUserByIdResults.Success,
        GetUserByIdResults.UserNotFound,
        GetUserByIdResults.InvalidRequest,
        GetUserByIdResults.SystemError
{

    record Success(@NotNull @JsonProperty("request_id") UUID requestId, @NotNull @Valid @JsonProperty("userId") UserDTO user, @Nullable @JsonProperty("tracing_id") String tracingId) implements GetUserByIdResults {}
    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId, @NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error, @Nullable @JsonProperty("tracing_id") String tracingId) implements GetUserByIdResults {}
    record UserNotFound(@NotNull @JsonProperty("request_id") UUID requestId, @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId, @Nullable @JsonProperty("tracing_id") String tracingId) implements GetUserByIdResults {}
    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId, @NotEmpty @JsonProperty("message") String message, @Nullable @JsonProperty("tracing_id") String tracingId) implements GetUserByIdResults { }
}
