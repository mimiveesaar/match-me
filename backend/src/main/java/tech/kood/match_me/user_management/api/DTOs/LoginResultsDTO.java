package tech.kood.match_me.user_management.api.DTOs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginResultsDTO.Success.class, name = "success"),
        @JsonSubTypes.Type(value = LoginResultsDTO.InvalidCredentials.class, name = "invalid_credentials"),
        @JsonSubTypes.Type(value = LoginResultsDTO.InvalidRequest.class, name = "invalid_request"),
        @JsonSubTypes.Type(value = LoginResultsDTO.SystemError.class, name = "system_error"),
})
@Schema(description = "Result of user registration", oneOf = {
        LoginResultsDTO.Success.class,
        LoginResultsDTO.InvalidCredentials.class,
        LoginResultsDTO.InvalidRequest.class,
        LoginResultsDTO.SystemError.class
})

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