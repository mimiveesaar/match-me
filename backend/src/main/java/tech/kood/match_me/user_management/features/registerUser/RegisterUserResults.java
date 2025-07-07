package tech.kood.match_me.user_management.features.registerUser;

import tech.kood.match_me.user_management.common.UserManagementResult;

public sealed interface RegisterUserResults extends UserManagementResult
    permits RegisterUserResults.Success, RegisterUserResults.UsernameExists,
        RegisterUserResults.EmailExists, RegisterUserResults.InvalidEmail, 
        RegisterUserResults.InvalidPasswordLength, RegisterUserResults.SystemError {

    record Success(String userId, String tracingId) implements RegisterUserResults {
            @Override
            public String tracingId() {
                return tracingId;
            }
    }

    record UsernameExists(String username, String tracingId)
        implements RegisterUserResults {
            @Override
            public String tracingId() {
                return tracingId;
            }
        }

    record EmailExists(String email, String tracingId) implements RegisterUserResults {
            @Override
            public String tracingId() {
                return tracingId;
            }
    }

    record InvalidEmail(String email, String tracingId) implements RegisterUserResults {
            @Override
            public String tracingId() {
                return tracingId;
            }
    }

    record InvalidPasswordLength(String password, String tracingId) implements RegisterUserResults {
            @Override
            public String tracingId() {
                return tracingId;
            }
    }

    record SystemError(String message, String tracingId) implements RegisterUserResults {
            @Override
            public String tracingId() {
                return tracingId;
            }
    }
}