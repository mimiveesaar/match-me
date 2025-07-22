package tech.kood.match_me.user_management.internal.features.refreshToken.createToken;

import java.util.Optional;
import java.util.UUID;

import tech.kood.match_me.user_management.models.RefreshToken;

public sealed interface CreateRefreshTokenResults permits CreateRefreshTokenResults.Success,
        CreateRefreshTokenResults.UserNotFound, CreateRefreshTokenResults.SystemError,
        CreateRefreshTokenResults.InvalidRequest {

    record Success(RefreshToken refreshToken) implements CreateRefreshTokenResults {
    }

    record UserNotFound(UUID userId, Optional<String> tracingId) implements CreateRefreshTokenResults {
    }

    record InvalidRequest(String message, Optional<String> tracingId) implements CreateRefreshTokenResults {
    }

    record SystemError(String message, Optional<String> tracingId) implements CreateRefreshTokenResults {
    }
}