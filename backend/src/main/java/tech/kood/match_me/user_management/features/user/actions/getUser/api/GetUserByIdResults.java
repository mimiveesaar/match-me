package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;

public sealed interface GetUserByIdResults permits
        GetUserByIdResults.Success,
        GetUserByIdResults.UserNotFound,
        GetUserByIdResults.InvalidRequest,
        GetUserByIdResults.SystemError
{

    record Success(@NotNull @Valid @JsonProperty("userId") UserDTO user) implements GetUserByIdResults {}
    record InvalidRequest(@NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error) implements GetUserByIdResults {}
    record UserNotFound(@NotNull @Valid @JsonProperty("user_id") UserIdDTO userId) implements GetUserByIdResults {}
    record SystemError(@NotEmpty @JsonProperty("message") String message) implements GetUserByIdResults { }
}
