package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.util.Optional;

import tech.kood.match_me.user_management.internal.common.UserManagementResult;


public sealed interface GetUserByEmailResults extends UserManagementResult permits
    GetUserByEmailResults.Success, GetUserByEmailResults.UserNotFound, GetUserByEmailResults.SystemError,
    GetUserByEmailResults.InvalidEmail {
    record Success(String userId, Optional<String> tracingId) implements GetUserByEmailResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    record UserNotFound(String email, Optional<String> tracingId) implements GetUserByEmailResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    record InvalidEmail(String email, Optional<String> tracingId) implements GetUserByEmailResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    record SystemError(String message, Optional<String> tracingId) implements GetUserByEmailResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }
}