package tech.kood.match_me.user_management.internal.features.user.features.getUser.getUserByEmail;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;

import java.util.UUID;

@ApplicationLayer
public record GetUserByEmailRequest(@NotNull UUID requestId, @NotNull @Valid Email email, @Nullable String tracingId) {

    public GetUserByEmailRequest withRequestId(UUID requestId) {
        return new GetUserByEmailRequest (requestId, email, tracingId);
    }

    public GetUserByEmailRequest withEmail(Email email) {
        return new GetUserByEmailRequest (requestId, email, tracingId);
    }

    public GetUserByEmailRequest withTracingId(String tracingId) {
        return new GetUserByEmailRequest (requestId, email, tracingId);
    }
}
