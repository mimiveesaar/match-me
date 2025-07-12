package tech.kood.match_me.user_management.DTOs;

import java.util.Optional;

public sealed interface RegisterUserResultsDTO
    permits RegisterUserResultsDTO.Success, RegisterUserResultsDTO.UsernameExists,
        RegisterUserResultsDTO.EmailExists, RegisterUserResultsDTO.InvalidEmail, 
        RegisterUserResultsDTO.InvalidPasswordLength, RegisterUserResultsDTO.SystemError {

    record Success(UserDTO user, Optional<String> tracingId) implements RegisterUserResultsDTO {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }

    record UsernameExists(String username, Optional<String> tracingId)
        implements RegisterUserResultsDTO {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
        }

    record EmailExists(String email, Optional<String> tracingId) implements RegisterUserResultsDTO {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }

    record InvalidEmail(String email, Optional<String> tracingId) implements RegisterUserResultsDTO {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }

    record InvalidPasswordLength(String password, Optional<String> tracingId) implements RegisterUserResultsDTO {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }

    record SystemError(String message, Optional<String> tracingId) implements RegisterUserResultsDTO {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }
}