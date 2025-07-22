package tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents a request to invalidate a refresh token.
 *
 * 
 * @param requestId the unique identifier for this request
 * @param token     the refresh token to be invalidated
 * @param tracingId an optional tracing identifier for request tracking
 */
public record InvalidateRefreshTokenRequest(
        UUID requestId,
        String token,
        Optional<String> tracingId) {

    public InvalidateRefreshTokenRequest withRequestId(UUID newRequestId) {
        return new InvalidateRefreshTokenRequest(newRequestId, token, tracingId);
    }

    public InvalidateRefreshTokenRequest withToken(String newToken) {
        return new InvalidateRefreshTokenRequest(requestId, newToken, tracingId);
    }

    public InvalidateRefreshTokenRequest withTracingId(Optional<String> newTracingId) {
        return new InvalidateRefreshTokenRequest(requestId, token, newTracingId);
    }
}