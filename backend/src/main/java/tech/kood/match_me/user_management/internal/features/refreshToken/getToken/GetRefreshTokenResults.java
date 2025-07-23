package tech.kood.match_me.user_management.internal.features.refreshToken.getToken;

import java.util.Optional;

import tech.kood.match_me.user_management.models.RefreshToken;

public sealed interface GetRefreshTokenResults {

    record Success(RefreshToken token, Optional<String> tracingId) implements GetRefreshTokenResults {
    }

    record InvalidRequest(String message, Optional<String> tracingId) implements GetRefreshTokenResults {
    }

    record InvalidToken(String message, Optional<String> tracingId) implements GetRefreshTokenResults {
    }
}
