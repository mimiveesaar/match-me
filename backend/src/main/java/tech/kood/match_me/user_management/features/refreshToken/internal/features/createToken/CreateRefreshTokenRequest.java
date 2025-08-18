package tech.kood.match_me.user_management.features.refreshToken.internal.features.createToken;

import java.util.UUID;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.User;

@Command
public record CreateRefreshTokenRequest(@NotNull UUID requestId, @NotNull User user, @Nullable String tracingId) {
}
