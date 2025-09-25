package tech.kood.match_me.user_management.features.user.actions.getUser.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GetUserByIdResults.Success.class, name = "SUCCESS"),
        @JsonSubTypes.Type(value = GetUserByIdResults.UserNotFound.class, name = "USER_NOT_FOUND"),
        @JsonSubTypes.Type(value = GetUserByIdResults.InvalidRequest.class, name = "INVALID_REQUEST"),
        @JsonSubTypes.Type(value = GetUserByIdResults.SystemError.class, name = "SYSTEM_ERROR")
})
public sealed interface GetUserByIdResults permits
        GetUserByIdResults.Success,
        GetUserByIdResults.UserNotFound,
        GetUserByIdResults.InvalidRequest,
        GetUserByIdResults.SystemError
{

    record Success(@NotNull @Valid @JsonProperty("user_id") UserDTO user) implements GetUserByIdResults {}
    record InvalidRequest(@NotNull @Valid @JsonProperty("data") InvalidInputErrorDTO error) implements GetUserByIdResults {}
    record UserNotFound(@NotNull @Valid @JsonProperty("user_id") UserIdDTO userId) implements GetUserByIdResults {}
    record SystemError(@NotEmpty @JsonProperty("message") String message) implements GetUserByIdResults { }
}
