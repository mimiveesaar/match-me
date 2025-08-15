package tech.kood.match_me.user_management.internal.features.user.registerUser;

import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;

@Command
@ApplicationLayer
public record RegisterUserRequest(@NotNull UUID requestId, @NotBlank String username, @NotBlank String password,
                                  @Email @NotBlank String email, @Nullable String tracingId) implements Serializable {

    public RegisterUserRequest withRequestId(UUID requestId) {
        return new RegisterUserRequest (requestId, username, password, email, tracingId);
    }

    public RegisterUserRequest withUsername(String username) {
        return new RegisterUserRequest (requestId, username, password, email, tracingId);
    }

    public RegisterUserRequest withPassword(String password) {
        return new RegisterUserRequest (requestId, username, password, email, tracingId);
    }

    public RegisterUserRequest withEmail(String email) {
        return new RegisterUserRequest (requestId, username, password, email, tracingId);
    }

    public RegisterUserRequest withTracingId(String tracingId) {
        return new RegisterUserRequest (requestId, username, password, email, tracingId);
    }
}