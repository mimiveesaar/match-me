package tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken;

import java.io.Serializable;
import jakarta.annotation.Nullable;
import tech.kood.match_me.user_management.internal.domain.models.RefreshToken;

public sealed interface CreateRefreshTokenResults extends Serializable
                permits CreateRefreshTokenResults.Success, CreateRefreshTokenResults.UserNotFound,
                CreateRefreshTokenResults.SystemError, CreateRefreshTokenResults.InvalidRequest {

        record Success(RefreshToken refreshToken) implements CreateRefreshTokenResults {
        }

        record UserNotFound(String userId, @Nullable String tracingId)
                        implements CreateRefreshTokenResults {
        }

        record SystemError(String message, @Nullable String tracingId)
                        implements CreateRefreshTokenResults {
        }
}
