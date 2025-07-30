package tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken;

import java.io.Serializable;

public sealed interface InvalidateRefreshTokenResults extends Serializable permits
                InvalidateRefreshTokenResults.Success, InvalidateRefreshTokenResults.TokenNotFound,
                InvalidateRefreshTokenResults.InvalidRequest,
                InvalidateRefreshTokenResults.SystemError {
        record Success() implements InvalidateRefreshTokenResults {
        }

        record TokenNotFound(String token, String tracingId)
                        implements InvalidateRefreshTokenResults {
        }

        record InvalidRequest(String message, String tracingId)
                        implements InvalidateRefreshTokenResults {
        }

        record SystemError(String message, String tracingId)
                        implements InvalidateRefreshTokenResults {
        }

}
