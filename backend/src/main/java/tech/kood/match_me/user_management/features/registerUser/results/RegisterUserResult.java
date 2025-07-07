package tech.kood.match_me.user_management.features.registerUser.results;

import tech.kood.match_me.user_management.common.UserManagementResult;

public sealed interface RegisterUserResult extends UserManagementResult
    permits RegisterUserResult.Success, RegisterUserResult.UsernameExists,
        RegisterUserResult.EmailExists, RegisterUserResult.InvalidEmail {

    record Success(String userId, String tracingId) implements RegisterUserResult {
            @Override
            public String tracingId() {
                return tracingId;
            }
    }

    record UsernameExists(String username, String tracingId)
        implements RegisterUserResult {
            @Override
            public String tracingId() {
                return tracingId;
            }
        }

    record EmailExists(String email, String tracingId) implements RegisterUserResult {
            @Override
            public String tracingId() {
                return tracingId;
            }
    }

    record InvalidEmail(String email, String tracingId) implements RegisterUserResult {
            @Override
            public String tracingId() {
                return tracingId;
            }
    }
}