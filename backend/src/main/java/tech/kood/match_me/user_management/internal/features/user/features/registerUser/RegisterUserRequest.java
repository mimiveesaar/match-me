package tech.kood.match_me.user_management.internal.features.user.features.registerUser;

import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;
import tech.kood.match_me.user_management.internal.features.user.domain.model.password.Password;

@Command
@ApplicationLayer
public record RegisterUserRequest(@NotNull UUID requestId, @Valid @NotNull Password password,
                                  @Valid @NotNull Email email, @Nullable String tracingId) {

    public RegisterUserRequest withRequestId(UUID requestId) {
        return new RegisterUserRequest (requestId, password, email, tracingId);
    }

    public RegisterUserRequest withPassword(Password password) {
        return new RegisterUserRequest (requestId, password, email, tracingId);
    }

    public RegisterUserRequest withEmail(Email email) {
        return new RegisterUserRequest (requestId, password, email, tracingId);
    }

    public RegisterUserRequest withTracingId(String tracingId) {
        return new RegisterUserRequest (requestId, password, email, tracingId);
    }
}