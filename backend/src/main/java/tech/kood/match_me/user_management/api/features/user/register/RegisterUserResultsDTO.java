
package tech.kood.match_me.user_management.api.features.user.register;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;


@Schema(name = "RegisterUserResultsDTO", oneOf = {
        RegisterUserResultsDTO.Success.class,
        RegisterUserResultsDTO.EmailExists.class,
        RegisterUserResultsDTO.SystemError.class})
public sealed interface RegisterUserResultsDTO
        permits RegisterUserResultsDTO.Success,
        RegisterUserResultsDTO.EmailExists,
        RegisterUserResultsDTO.SystemError {


    @Schema(name = "RegisterUserEmailExistsDTO")
    record EmailExists(@Email String email, @NotEmpty String kind,
                       @Nullable String tracingId) implements RegisterUserResultsDTO {
        public EmailExists(String email, String tracingId) {
            this(email, "email_exists", tracingId);
        }

    }

    @Schema(name = "RegisterUserSuccessDTO")
    record Success(@Nonnull UserDTO user, @NotEmpty String kind,
                   @Nullable String tracingId) implements RegisterUserResultsDTO {
        public Success(UserDTO user, String tracingId) {
            this(user, "success", tracingId);
        }
    }

    @Schema(name = "RegisterUserSystemErrorDTO")
    record SystemError(@NotEmpty String message, @NotEmpty String kind,
                       @Nullable String tracingId) implements RegisterUserResultsDTO {
        public SystemError(String message, String tracingId) {
            this(message, "system_error", tracingId);
        }
    }
}
