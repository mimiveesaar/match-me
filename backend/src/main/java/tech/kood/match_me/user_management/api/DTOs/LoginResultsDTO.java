package tech.kood.match_me.user_management.api.DTOs;

public sealed interface LoginResultsDTO
        permits LoginResultsDTO.Success,
        LoginResultsDTO.InvalidCredentials,
        LoginResultsDTO.InvalidRequest,
        LoginResultsDTO.SystemError {

    record Success(String token) implements LoginResultsDTO {
    }

    record InvalidCredentials(String username, String password) implements LoginResultsDTO {
    }

    record InvalidRequest(String message) implements LoginResultsDTO {
        public InvalidRequest() {
            this("Invalid login request.");
        }
    }

    record SystemError(String message) implements LoginResultsDTO {
        public SystemError() {
            this("An unexpected error occurred during login.");
        }
    }
}