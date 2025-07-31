
package tech.kood.match_me.user_management.api.DTOs;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;



@Schema(name = "RegisterUserResultsDTO", oneOf = {RegisterUserResultsDTO.Success.class,
                RegisterUserResultsDTO.UsernameExists.class,
                RegisterUserResultsDTO.EmailExists.class, RegisterUserResultsDTO.InvalidEmail.class,
                RegisterUserResultsDTO.InvalidPassword.class,
                RegisterUserResultsDTO.InvalidUsername.class,
                RegisterUserResultsDTO.SystemError.class})
public sealed interface RegisterUserResultsDTO extends Serializable
                permits RegisterUserResultsDTO.Success, RegisterUserResultsDTO.UsernameExists,
                RegisterUserResultsDTO.EmailExists, RegisterUserResultsDTO.InvalidEmail,
                RegisterUserResultsDTO.InvalidPassword, RegisterUserResultsDTO.InvalidUsername,
                RegisterUserResultsDTO.SystemError {


        @Schema(name = "RegisterUserEmailExistsDTO")
        public record EmailExists(String email, String kind, String tracingId)
                        implements RegisterUserResultsDTO {
                public EmailExists(String email, String tracingId) {
                        this(email, "email_exists", tracingId);
                }

        }

        @Schema(name = "RegisterUserInvalidEmailDTO")
        public record InvalidEmail(String email, String kind, String tracingId)
                        implements RegisterUserResultsDTO {
                public InvalidEmail(String email, String tracingId) {
                        this(email, "invalid_email", tracingId);
                }
        }

        @Schema(name = "RegisterUserInvalidPasswordDTO")
        public record InvalidPassword(String password, InvalidPasswordType type, String kind,
                        String tracingId) implements RegisterUserResultsDTO {

                public InvalidPassword(String password, InvalidPasswordType type,
                                String tracingId) {
                        this(password, type, "invalid_password", tracingId);
                }
        }

        @Schema(name = "RegisterUserInvalidUsernameTypeDTO")
        public enum InvalidUsernameType {
                TOO_SHORT, TOO_LONG, INVALID_CHARACTERS
        }

        @Schema(name = "RegisterUserInvalidUsernameTypeDTO")
        public enum InvalidPasswordType {
                TOO_SHORT, TOO_LONG, WEAK
        }

        @Schema(name = "RegisterUserInvalidUsernameDTO")
        public record InvalidUsername(String username, String kind, InvalidUsernameType type,
                        String tracingId) implements RegisterUserResultsDTO {

                public InvalidUsername(String username, InvalidUsernameType type,
                                String tracingId) {
                        this(username, "invalid_username", type, tracingId);
                }
        }

        @Schema(name = "RegisterUserSuccessDTO")
        public record Success(UserDTO user, String kind, String tracingId)
                        implements RegisterUserResultsDTO {
                public Success(UserDTO user, String tracingId) {
                        this(user, "success", tracingId);
                }
        }

        @Schema(name = "RegisterUserSystemErrorDTO")
        public record SystemError(String message, String kind, String tracingId)
                        implements RegisterUserResultsDTO {
                public SystemError(String message, String tracingId) {
                        this(message, "system_error", tracingId);
                }
        }

        @Schema(name = "RegisterUserUsernameExistsDTO")
        public record UsernameExists(String username, String kind, String tracingId)
                        implements RegisterUserResultsDTO {


                public UsernameExists(String username, String tracingId) {
                        this(username, "username_exists", tracingId);
                }
        }
}
