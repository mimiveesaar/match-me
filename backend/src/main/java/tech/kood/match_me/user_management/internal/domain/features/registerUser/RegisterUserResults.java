package tech.kood.match_me.user_management.internal.domain.features.registerUser;

import java.io.Serializable;
import jakarta.annotation.Nullable;
import tech.kood.match_me.user_management.internal.domain.models.User;


public sealed interface RegisterUserResults extends Serializable
        permits RegisterUserResults.Success, RegisterUserResults.UsernameExists,
        RegisterUserResults.EmailExists, RegisterUserResults.InvalidEmail,
        RegisterUserResults.InvalidPassword, RegisterUserResults.InvalidUsername,
        RegisterUserResults.SystemError {

    record Success(User user, @Nullable String tracingId) implements RegisterUserResults {
    }

    record UsernameExists(String username, @Nullable String tracingId)
            implements RegisterUserResults {
    }

    record EmailExists(String email, @Nullable String tracingId) implements RegisterUserResults {
    }

    record InvalidEmail(String email, @Nullable String tracingId) implements RegisterUserResults {
    }

    enum InvalidUsernameType {
        TOO_SHORT, TOO_LONG, INVALID_CHARACTERS
    }

    record InvalidUsername(String username, InvalidUsernameType type, String tracingId)
            implements RegisterUserResults {
    }

    enum InvalidPasswordType {
        TOO_SHORT, TOO_LONG, WEAK
    }

    record InvalidPassword(String password, InvalidPasswordType type, @Nullable String tracingId)
            implements RegisterUserResults {
    }

    record SystemError(String message, @Nullable String tracingId) implements RegisterUserResults {
    }
}
