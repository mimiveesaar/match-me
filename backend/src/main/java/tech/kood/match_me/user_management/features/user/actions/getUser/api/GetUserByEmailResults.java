package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;

public sealed interface GetUserByEmailResults
        permits
        GetUserByEmailResults.Success,
        GetUserByEmailResults.UserNotFound,
        GetUserByEmailResults.InvalidRequest,
        GetUserByEmailResults.SystemError  {

    record Success(@NotNull @Valid @JsonProperty("userId") UserDTO user) implements GetUserByEmailResults {}
    record InvalidRequest(@NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error) implements GetUserByEmailResults {}
    record UserNotFound(@NotNull @Valid @JsonProperty("email") EmailDTO email) implements GetUserByEmailResults {}
    record SystemError(@NotEmpty @JsonProperty("message") String message) implements GetUserByEmailResults {}
}
