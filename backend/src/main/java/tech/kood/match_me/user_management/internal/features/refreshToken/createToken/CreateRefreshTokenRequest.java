package tech.kood.match_me.user_management.internal.features.refreshToken.createToken;

import java.util.Optional;
import java.util.UUID;

import tech.kood.match_me.user_management.models.User;

/**
 * Represents a request to refresh a token, containing identifiers for the
 * request,
 * the user, and an optional tracing ID for tracking purposes.
 *
 * @param requestId the unique identifier for this refresh token request
 * @param user      user for whom the refresh token is being created.
 * @param tracingId an optional tracing identifier for request tracking and
 *                  debugging
 *
 */
public record CreateRefreshTokenRequest(
        UUID requestId,
        User user,
        Optional<String> tracingId) {

    public CreateRefreshTokenRequest withRequestId(UUID newRequestId) {
        return new CreateRefreshTokenRequest(newRequestId, user, tracingId);
    }

    public CreateRefreshTokenRequest withUser(User newUser) {
        return new CreateRefreshTokenRequest(requestId, newUser, tracingId);
    }

    public CreateRefreshTokenRequest withTracingId(Optional<String> newTracingId) {
        return new CreateRefreshTokenRequest(requestId, user, newTracingId);
    }
}