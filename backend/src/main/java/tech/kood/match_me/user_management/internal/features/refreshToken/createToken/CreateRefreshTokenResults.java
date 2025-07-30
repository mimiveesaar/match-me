package tech.kood.match_me.user_management.internal.features.refreshToken.createToken;

import java.io.Serializable;
import jakarta.annotation.Nullable;
import tech.kood.match_me.user_management.models.RefreshToken;

public sealed interface CreateRefreshTokenResults extends Serializable
        permits CreateRefreshTokenResults.Success, CreateRefreshTokenResults.UserNotFound,
        CreateRefreshTokenResults.SystemError, CreateRefreshTokenResults.InvalidRequest {

    record Success(RefreshToken refreshToken) implements CreateRefreshTokenResults {
    }

    record UserNotFound(String userId, @Nullable String tracingId)
            implements CreateRefreshTokenResults {
    }

    record InvalidRequest(String message, @Nullable String tracingId)
            implements CreateRefreshTokenResults {
    }

    record SystemError(String message, @Nullable String tracingId)
            implements CreateRefreshTokenResults {
    }
}
