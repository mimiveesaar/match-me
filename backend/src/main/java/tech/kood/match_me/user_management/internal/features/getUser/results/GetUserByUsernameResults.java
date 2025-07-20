package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.util.Optional;

import tech.kood.match_me.user_management.api.DTOs.UserDTO;
import tech.kood.match_me.user_management.internal.common.UserManagementResult;

public sealed interface GetUserByUsernameResults extends UserManagementResult permits
    GetUserByUsernameResults.Success, GetUserByUsernameResults.UserNotFound, GetUserByUsernameResults.SystemError,
    GetUserByUsernameResults.InvalidUsername {
    
    /**
     * Represents a successful result of fetching a user by username.
     *
     * @param user      The {@link UserDTO} object containing user details.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record Success(UserDTO user, Optional<String> tracingId) implements GetUserByUsernameResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result when a user is not found by their username.
     *
     * @param username  The username of the user that was not found.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record UserNotFound(String username, Optional<String> tracingId) implements GetUserByUsernameResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result of a failed attempt to retrieve a user by username due to an invalid username.
     *
     * @param username  The username that was invalid.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record InvalidUsername(String username, Optional<String> tracingId) implements GetUserByUsernameResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents a system error result for the GetUserByUsername operation.
     *
     * @param message   The error message describing the system error.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record SystemError(String message, Optional<String> tracingId) implements GetUserByUsernameResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }
}

