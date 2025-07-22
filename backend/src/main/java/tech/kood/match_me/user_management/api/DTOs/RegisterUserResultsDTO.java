package tech.kood.match_me.user_management.api.DTOs;

import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = RegisterUserResultsDTO.Success.class, name = "success"),
    @JsonSubTypes.Type(value = RegisterUserResultsDTO.UsernameExists.class, name = "username_exists"),
    @JsonSubTypes.Type(value = RegisterUserResultsDTO.EmailExists.class, name = "email_exists"),
    @JsonSubTypes.Type(value = RegisterUserResultsDTO.InvalidEmail.class, name = "invalid_email"),
    @JsonSubTypes.Type(value = RegisterUserResultsDTO.InvalidPassword.class, name = "invalid_password"),
    @JsonSubTypes.Type(value = RegisterUserResultsDTO.InvalidUsername.class, name = "invalid_username"),
    @JsonSubTypes.Type(value = RegisterUserResultsDTO.SystemError.class, name = "system_error")
})

@Schema(
    description = "Result of user registration",
    oneOf = {
        RegisterUserResultsDTO.Success.class,
        RegisterUserResultsDTO.UsernameExists.class,
        RegisterUserResultsDTO.EmailExists.class,
        RegisterUserResultsDTO.InvalidEmail.class,
        RegisterUserResultsDTO.InvalidPassword.class,
        RegisterUserResultsDTO.InvalidUsername.class,
        RegisterUserResultsDTO.SystemError.class
    }
)
public sealed interface RegisterUserResultsDTO
    permits RegisterUserResultsDTO.Success, RegisterUserResultsDTO.UsernameExists,
        RegisterUserResultsDTO.EmailExists, RegisterUserResultsDTO.InvalidEmail, 
        RegisterUserResultsDTO.InvalidPassword, RegisterUserResultsDTO.InvalidUsername,
        RegisterUserResultsDTO.SystemError {

    record Success(UserDTO user, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }

    record UsernameExists(String username, Optional<String> tracingId)
        implements RegisterUserResultsDTO {
        }

    record EmailExists(String email, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }

    record InvalidEmail(String email, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }

    enum InvalidUsernameType {
        TOO_SHORT, TOO_LONG, INVALID_CHARACTERS
    }

    record InvalidUsername(String username, InvalidUsernameType type, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }

    enum InvalidPasswordType {
        TOO_SHORT, TOO_LONG, WEAK
    }

    record InvalidPassword(String password, InvalidPasswordType type, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }

    record SystemError(String message, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }
}