package tech.kood.match_me.user_management.api.DTOs;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;


@Schema(name = "LoginResultsDTO",
        oneOf = {LoginResultsDTO.Success.class, LoginResultsDTO.InvalidCredentials.class,
                LoginResultsDTO.InvalidRequest.class, LoginResultsDTO.SystemError.class})
public sealed interface LoginResultsDTO extends Serializable
        permits LoginResultsDTO.Success, LoginResultsDTO.InvalidCredentials,
        LoginResultsDTO.InvalidRequest, LoginResultsDTO.SystemError {

    @Schema(name = "LoginResultsSuccessDTO")
    record Success(@NotEmpty String refreshToken, @NotEmpty String kind, @Nullable String tracingId)
            implements LoginResultsDTO {
        public Success(String refreshToken, String tracingId) {
            this(refreshToken, "success", tracingId);
        }
    }

    @Schema(name = "LoginResultsInvalidCredentialsDTO")
    record InvalidCredentials(@NotEmpty String username, @NotEmpty String password,
            @NotEmpty String kind, @Nullable String tracingId) implements LoginResultsDTO {
        public InvalidCredentials(String username, String password, String tracingId) {
            this(username, password, "invalid_credentials", tracingId);
        }
    }

    @Schema(name = "LoginResultsInvalidRequestDTO")
    record InvalidRequest(String message, String kind, @Nullable String tracingId)
            implements LoginResultsDTO {
        public InvalidRequest(String tracingId) {
            this("Invalid login request.", "invalid_request", tracingId);
        }

        public InvalidRequest(String message, String tracingId) {
            this(message, "invalid_request", tracingId);
        }
    }

    @Schema(name = "LoginResultsSystemErrorDTO")
    record SystemError(String message, String kind, @Nullable String tracingId)
            implements LoginResultsDTO {
        public SystemError(String tracingId) {
            this("An unexpected error occurred during login.", "system_error", tracingId);
        }

        public SystemError(String message, String tracingId) {
            this(message, "system_error", tracingId);
        }
    }
}
