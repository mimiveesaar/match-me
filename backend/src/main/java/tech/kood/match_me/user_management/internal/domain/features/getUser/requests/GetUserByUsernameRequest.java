package tech.kood.match_me.user_management.internal.domain.features.getUser.requests;

import jakarta.annotation.Nullable;

/**
 * Request object for retrieving a user by username.
 * 
 * @param requestId Unique identifier for the request.
 * @param username The username of the user to retrieve.
 * @param tracingId Optional tracing identifier for request tracking.
 *
 */
public record GetUserByUsernameRequest(String requestId, String username,
        @Nullable String tracingId) {
    GetUserByUsernameRequest withUsername(String newUsername) {
        return new GetUserByUsernameRequest(requestId, newUsername, tracingId);
    }

    GetUserByUsernameRequest withTracingId(String newTracingId) {
        return new GetUserByUsernameRequest(requestId, username, newTracingId);
    }
}
