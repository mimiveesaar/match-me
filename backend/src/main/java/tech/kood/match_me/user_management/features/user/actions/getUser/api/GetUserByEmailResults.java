package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GetUserByEmailResults.Success.class, name = "SUCCESS"),
        @JsonSubTypes.Type(value = GetUserByEmailResults.UserNotFound.class, name = "USER_NOT_FOUND"),
        @JsonSubTypes.Type(value = GetUserByEmailResults.InvalidRequest.class, name = "INVALID_REQUEST"),
        @JsonSubTypes.Type(value = GetUserByEmailResults.SystemError.class, name = "SYSTEM_ERROR")
})

public sealed interface GetUserByEmailResults
        permits
        GetUserByEmailResults.Success,
        GetUserByEmailResults.UserNotFound,
        GetUserByEmailResults.InvalidRequest,
        GetUserByEmailResults.SystemError  {

    record Success(@NotNull @Valid @JsonProperty("user_id") UserDTO user) implements GetUserByEmailResults {}
    record InvalidRequest(@NotNull @Valid @JsonProperty("data") InvalidInputErrorDTO error) implements GetUserByEmailResults {}
    record UserNotFound(@NotNull @Valid @JsonProperty("email") EmailDTO email) implements GetUserByEmailResults {}
    record SystemError(@NotEmpty @JsonProperty("message") String message) implements GetUserByEmailResults {}
}
