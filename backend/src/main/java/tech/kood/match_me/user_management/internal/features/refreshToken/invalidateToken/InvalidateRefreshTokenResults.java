package tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken;

import java.io.Serializable;
import jakarta.annotation.Nullable;

public sealed interface InvalidateRefreshTokenResults extends Serializable permits
                InvalidateRefreshTokenResults.Success, InvalidateRefreshTokenResults.TokenNotFound,
                InvalidateRefreshTokenResults.InvalidRequest,
                InvalidateRefreshTokenResults.SystemError {
        record Success(@Nullable String tracingId) implements InvalidateRefreshTokenResults {
        }

        record TokenNotFound(String token, @Nullable String tracingId)
                        implements InvalidateRefreshTokenResults {
        }

        record InvalidRequest(String message, @Nullable String tracingId)
                        implements InvalidateRefreshTokenResults {
        }

        record SystemError(String message, @Nullable String tracingId)
                        implements InvalidateRefreshTokenResults {
        }
}
