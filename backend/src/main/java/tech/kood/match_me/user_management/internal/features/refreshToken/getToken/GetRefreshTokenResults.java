package tech.kood.match_me.user_management.internal.features.refreshToken.getToken;


import jakarta.annotation.Nullable;
import tech.kood.match_me.user_management.models.RefreshToken;

public sealed interface GetRefreshTokenResults {

    record Success(RefreshToken token, @Nullable String tracingId)
            implements GetRefreshTokenResults {
    }

    record InvalidRequest(String message, @Nullable String tracingId)
            implements GetRefreshTokenResults {
    }

    record InvalidToken(String token, @Nullable String tracingId)
            implements GetRefreshTokenResults {
    }

    record SystemError(String message, @Nullable String tracingId)
            implements GetRefreshTokenResults {
    }
}
