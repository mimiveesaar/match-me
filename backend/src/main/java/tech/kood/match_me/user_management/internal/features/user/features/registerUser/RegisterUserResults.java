package tech.kood.match_me.user_management.internal.features.user.features.registerUser;

import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;

@QueryModel
@ApplicationLayer
public sealed interface RegisterUserResults permits
        RegisterUserResults.Success,
        RegisterUserResults.EmailExists,
        RegisterUserResults.SystemError {

    record Success(@NotNull UUID requestId, @NotNull UserId userId,
                   @Nullable String tracingId) implements RegisterUserResults {
    }

    record EmailExists(@NotNull UUID requestId, @NotNull Email email,
                       @Nullable String tracingId) implements RegisterUserResults {
    }

    record SystemError(@NotNull UUID requestId, @NotEmpty String message,
                       @Nullable String tracingId) implements RegisterUserResults {
    }
}