package tech.kood.match_me.user_management.internal.features.refreshToken.features.createToken;

import java.util.UUID;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;

@Command
public record CreateRefreshTokenRequest(@NotNull UUID requestId, @NotNull User user, @Nullable String tracingId) {
}
