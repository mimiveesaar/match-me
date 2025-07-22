package tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken;

import java.util.Optional;

public interface InvalidateRefreshTokenResults {
    record Success(String message) implements InvalidateRefreshTokenResults {
    }

    record TokenNotFound(String token, Optional<String> tracingId) implements InvalidateRefreshTokenResults {
    }

    record InvalidRequest(String message, Optional<String> tracingId) implements InvalidateRefreshTokenResults {
    }

    record SystemError(String message, Optional<String> tracingId) implements InvalidateRefreshTokenResults {
    }

}