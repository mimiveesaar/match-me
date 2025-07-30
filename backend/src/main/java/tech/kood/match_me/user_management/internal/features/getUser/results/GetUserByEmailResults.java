package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.io.Serializable;
import jakarta.annotation.Nullable;

import tech.kood.match_me.user_management.models.User;


public sealed interface GetUserByEmailResults extends Serializable
        permits GetUserByEmailResults.Success, GetUserByEmailResults.UserNotFound,
        GetUserByEmailResults.SystemError, GetUserByEmailResults.InvalidEmail {

    /**
     * Represents a successful result of fetching a user by email.
     *
     * @param user The {@link User} object containing user details.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record Success(User user, String requestId, @Nullable String tracingId)
            implements GetUserByEmailResults {
        @Override
        public String tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result when a user is not found by their email.
     *
     * @param email The email of the user that was not found.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record UserNotFound(String email, String requestId, @Nullable String tracingId)
            implements GetUserByEmailResults {
        @Override
        public String tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents the result of a failed attempt to retrieve a user by email due to an invalid email
     * format.
     *
     * @param email The email that was invalid.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record InvalidEmail(String email, String requestId, @Nullable String tracingId)
            implements GetUserByEmailResults {
        @Override
        public String tracingId() {
            return tracingId;
        }
    }

    /**
     * Represents a system error result for the GetUserByEmail operation.
     *
     * @param message The error message describing the system error.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    record SystemError(String message, String requestId, @Nullable String tracingId)
            implements GetUserByEmailResults {
        @Override
        public String tracingId() {
            return tracingId;
        }
    }
}
