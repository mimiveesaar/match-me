package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.util.Optional;
import java.util.UUID;

import tech.kood.match_me.user_management.api.DTOs.UserDTO;
import tech.kood.match_me.user_management.internal.common.UserManagementResult;

public sealed interface GetUserByIdResults extends UserManagementResult permits
    GetUserByIdResults.Success, GetUserByIdResults.UserNotFound, GetUserByIdResults.SystemError,
    GetUserByIdResults.InvalidUserId
    {
    
    /**
     * Represents a successful result of fetching a user by ID.
     *
     * @param user      The {@link UserDTO} object containing user details.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record Success(UserDTO user, UUID requestId, Optional<String> tracingId) implements GetUserByIdResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result when a user is not found by their ID.
     *
     * @param id        The UUID of the user that was not found.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record UserNotFound(UUID id, UUID requestId, Optional<String> tracingId) implements GetUserByIdResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result of a failed attempt to retrieve a user by ID due to an invalid user ID.
     *
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record InvalidUserId(UUID requestId, Optional<String> tracingId) implements GetUserByIdResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents a system error result for the GetUserById operation.
     *
     * @param message   The error message describing the system error.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record SystemError(String message, UUID requestId, Optional<String> tracingId) implements GetUserByIdResults {
        @Override
        public Optional<String> tracingId() {
            return tracingId;
        }
    }
}


