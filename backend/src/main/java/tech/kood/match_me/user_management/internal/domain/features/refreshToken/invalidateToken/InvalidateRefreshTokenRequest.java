package tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken;

import java.io.Serializable;

/**
 * Represents a request to invalidate a refresh token.
 *
 * 
 * @param requestId the unique identifier for this request
 * @param jwt the refresh token to be invalidated
 * @param tracingId an optional tracing identifier for request tracking
 */
public record InvalidateRefreshTokenRequest(String requestId, String token, String tracingId)
        implements Serializable {

    public InvalidateRefreshTokenRequest withRequestId(String newRequestId) {
        return new InvalidateRefreshTokenRequest(newRequestId, token, tracingId);
    }

    public InvalidateRefreshTokenRequest withToken(String newToken) {
        return new InvalidateRefreshTokenRequest(requestId, newToken, tracingId);
    }

    public InvalidateRefreshTokenRequest withTracingId(String newTracingId) {
        return new InvalidateRefreshTokenRequest(requestId, token, newTracingId);
    }
}
