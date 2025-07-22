package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.util.Optional;
import java.util.UUID;

import tech.kood.match_me.user_management.internal.common.UserManagementResult;
import tech.kood.match_me.user_management.models.User;


public sealed interface GetUserByEmailResults extends UserManagementResult permits
    GetUserByEmailResults.Success, GetUserByEmailResults.UserNotFound, GetUserByEmailResults.SystemError,
    GetUserByEmailResults.InvalidEmail {

    /**
     * Represents a successful result of fetching a user by email.
     *
     * @param user      The {@link User} object containing user details.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record Success(User user, UUID requestId, Optional<String> tracingId) implements GetUserByEmailResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result when a user is not found by their email.
     *
     * @param email     The email of the user that was not found.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record UserNotFound(String email, UUID requestId, Optional<String> tracingId) implements GetUserByEmailResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result of a failed attempt to retrieve a user by email due to an invalid email format.
     *
     * @param email     The email that was invalid.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record InvalidEmail(String email, UUID requestId, Optional<String> tracingId) implements GetUserByEmailResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents a system error result for the GetUserByEmail operation.
     *
     * @param message   The error message describing the system error.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record SystemError(String message, UUID requestId, Optional<String> tracingId) implements GetUserByEmailResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }
}