package tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken;


import jakarta.annotation.Nullable;
import tech.kood.match_me.user_management.internal.domain.models.RefreshToken;

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
