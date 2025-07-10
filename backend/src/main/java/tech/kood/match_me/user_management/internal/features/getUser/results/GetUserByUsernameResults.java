package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.util.Optional;

import tech.kood.match_me.user_management.internal.common.UserManagementResult;
import tech.kood.match_me.user_management.models.User;

public sealed interface GetUserByUsernameResults extends UserManagementResult permits
    GetUserByUsernameResults.Success, GetUserByUsernameResults.UserNotFound, GetUserByUsernameResults.SystemError,
    GetUserByUsernameResults.InvalidUsername {
    
    record Success(User user, Optional<String> tracingId) implements GetUserByUsernameResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    record UserNotFound(String username, Optional<String> tracingId) implements GetUserByUsernameResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    record InvalidUsername(String username, Optional<String> tracingId) implements GetUserByUsernameResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    record SystemError(String message, Optional<String> tracingId) implements GetUserByUsernameResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }
}

