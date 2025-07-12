package tech.kood.match_me.user_management.internal.features.registerUser;

import java.util.Optional;

import tech.kood.match_me.user_management.internal.common.UserManagementResult;
import tech.kood.match_me.user_management.models.User;

public sealed interface RegisterUserResults extends UserManagementResult
    permits RegisterUserResults.Success, RegisterUserResults.UsernameExists,
        RegisterUserResults.EmailExists, RegisterUserResults.InvalidEmail, 
        RegisterUserResults.InvalidPasswordLength, RegisterUserResults.SystemError {

    record Success(User user, Optional<String> tracingId) implements RegisterUserResults {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }

    record UsernameExists(String username, Optional<String> tracingId)
        implements RegisterUserResults {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
        }

    record EmailExists(String email, Optional<String> tracingId) implements RegisterUserResults {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }

    record InvalidEmail(String email, Optional<String> tracingId) implements RegisterUserResults {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }

    record InvalidPasswordLength(String password, Optional<String> tracingId) implements RegisterUserResults {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }

    record SystemError(String message, Optional<String> tracingId) implements RegisterUserResults {
            @Override
            public Optional<String> tracingId() {
                return tracingId;
            }
    }
}