package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.util.Optional;
import java.util.UUID;

import tech.kood.match_me.user_management.internal.common.UserManagementResult;
import tech.kood.match_me.user_management.models.User;

public sealed interface GetUserByIdResults extends UserManagementResult permits
    GetUserByIdResults.Success, GetUserByIdResults.UserNotFound, GetUserByIdResults.SystemError,
    GetUserByIdResults.InvalidUserId
    {
    
    record Success(User user, Optional<String> tracingId) implements GetUserByIdResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    record UserNotFound(UUID id, Optional<String> tracingId) implements GetUserByIdResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    record InvalidUserId(Optional<String> tracingId) implements GetUserByIdResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    record SystemError(String message, Optional<String> tracingId) implements GetUserByIdResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }
}


