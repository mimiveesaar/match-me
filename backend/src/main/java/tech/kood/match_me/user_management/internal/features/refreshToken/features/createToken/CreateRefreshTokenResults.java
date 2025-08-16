package tech.kood.match_me.user_management.internal.features.refreshToken.features.createToken;

import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.internal.features.refreshToken.domain.model.RefreshToken;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;

@ApplicationLayer
@QueryModel
public sealed interface CreateRefreshTokenResults
        permits CreateRefreshTokenResults.Success, CreateRefreshTokenResults.UserNotFound,
        CreateRefreshTokenResults.SystemError {

    record Success(@NotNull UUID requestId, RefreshToken refreshToken, @Nullable String tracingId) implements CreateRefreshTokenResults {
    }

    record UserNotFound(@NotNull UUID requestId, @NotEmpty UserId userId, @Nullable String tracingId) implements CreateRefreshTokenResults {
    }

    record SystemError(@NotNull UUID requestId, @NotEmpty String message, @Nullable String tracingId) implements CreateRefreshTokenResults {
    }
}
