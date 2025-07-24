package tech.kood.match_me.user_management.api.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

public sealed interface LoginResultsDTO
        permits LoginResultsDTO.Success, LoginResultsDTO.InvalidCredentials,
        LoginResultsDTO.InvalidRequest, LoginResultsDTO.SystemError {

    @Schema(name = "LoginResultsSuccessDTO")
    record Success(String token, String kind) implements LoginResultsDTO {
        public Success(String token) {
            this(token, "success");
        }
    }

    @Schema(name = "LoginResultsInvalidCredentialsDTO")
    record InvalidCredentials(String username, String password, String kind)
            implements LoginResultsDTO {
        public InvalidCredentials(String username, String password) {
            this(username, password, "invalid_credentials");
        }
    }

    @Schema(name = "LoginResultsInvalidRequestDTO")
    record InvalidRequest(String message, String kind) implements LoginResultsDTO {
        public InvalidRequest() {
            this("Invalid login request.", "invalid_request");
        }

        public InvalidRequest(String message) {
            this(message, "invalid_request");
        }
    }

    @Schema(name = "LoginResultsSystemErrorDTO")
    record SystemError(String message, String kind) implements LoginResultsDTO {
        public SystemError() {
            this("An unexpected error occurred during login.", "system_error");
        }

        public SystemError(String message) {
            this(message, "system_error");
        }
    }
}
