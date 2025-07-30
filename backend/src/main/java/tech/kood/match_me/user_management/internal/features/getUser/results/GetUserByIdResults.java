package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.io.Serializable;
import jakarta.annotation.Nullable;
import tech.kood.match_me.user_management.models.User;

public sealed interface GetUserByIdResults extends Serializable
        permits GetUserByIdResults.Success, GetUserByIdResults.UserNotFound,
        GetUserByIdResults.SystemError, GetUserByIdResults.InvalidUserId {

    /**
     * Represents a successful result of fetching a user by ID.
     *
     * @param user The {@link User} object containing user details.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record Success(User user, String requestId, @Nullable String tracingId)
            implements GetUserByIdResults {
        @Override
        public String tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result when a user is not found by their ID.
     *
     * @param id The UUID of the user that was not found.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record UserNotFound(String id, String requestId, @Nullable String tracingId)
            implements GetUserByIdResults {
        @Override
        public String tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result of a failed attempt to retrieve a user by ID due to an invalid user ID.
     *
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record InvalidUserId(String requestId, @Nullable String tracingId)
            implements GetUserByIdResults {
        @Override
        public String tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents a system error result for the GetUserById operation.
     *
     * @param message The error message describing the system error.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record SystemError(String message, String requestId, @Nullable String tracingId)
            implements GetUserByIdResults {
        @Override
        public String tracingId() {
            return tracingId;
        }
    }
}


