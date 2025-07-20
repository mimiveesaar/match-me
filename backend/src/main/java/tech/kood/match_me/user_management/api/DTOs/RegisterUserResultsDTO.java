package tech.kood.match_me.user_management.api.DTOs;

import java.util.Optional;

public sealed interface RegisterUserResultsDTO
    permits RegisterUserResultsDTO.Success, RegisterUserResultsDTO.UsernameExists,
        RegisterUserResultsDTO.EmailExists, RegisterUserResultsDTO.InvalidEmail, 
        RegisterUserResultsDTO.InvalidPasswordLength, RegisterUserResultsDTO.SystemError {

    record Success(UserDTO user, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }

    record UsernameExists(String username, Optional<String> tracingId)
        implements RegisterUserResultsDTO {
        }

    record EmailExists(String email, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }

    record InvalidEmail(String email, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }

    record InvalidPasswordLength(String password, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }

    record SystemError(String message, Optional<String> tracingId) implements RegisterUserResultsDTO {
    }
}