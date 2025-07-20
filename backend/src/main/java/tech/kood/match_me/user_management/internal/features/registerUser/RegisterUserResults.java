package tech.kood.match_me.user_management.internal.features.registerUser;

import java.util.Optional;

import tech.kood.match_me.user_management.internal.common.UserManagementResult;
import tech.kood.match_me.user_management.models.User;


public sealed interface RegisterUserResults extends UserManagementResult
    permits RegisterUserResults.Success, RegisterUserResults.UsernameExists,
        RegisterUserResults.EmailExists, RegisterUserResults.InvalidEmail, 
        RegisterUserResults.InvalidPassword, RegisterUserResults.InvalidUsername,
        RegisterUserResults.SystemError {

    record Success(User user, Optional<String> tracingId) implements RegisterUserResults {
    }

    record UsernameExists(String username, Optional<String> tracingId) implements RegisterUserResults {
    }

    record EmailExists(String email, Optional<String> tracingId) implements RegisterUserResults {
    }

    record InvalidEmail(String email, Optional<String> tracingId) implements RegisterUserResults {
    }

    enum InvalidUsernameType {
        TOO_SHORT, TOO_LONG, INVALID_CHARACTERS
    }

    record InvalidUsername(String username, InvalidUsernameType type, Optional<String> tracingId) implements RegisterUserResults {
    }

    enum InvalidPasswordType {
        TOO_SHORT, TOO_LONG, WEAK
    }

    record InvalidPassword(String password, InvalidPasswordType type, Optional<String> tracingId) implements RegisterUserResults {
    }

    record SystemError(String message, Optional<String> tracingId) implements RegisterUserResults {
    }
}