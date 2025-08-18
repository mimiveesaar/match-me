package tech.kood.match_me.user_management.features.user.features.registerUser.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.features.user.domain.api.EmailDTO;
import tech.kood.match_me.user_management.features.user.domain.api.PasswordDTO;

@Command
@ApplicationLayer
public record RegisterUserRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                  @Valid @NotNull @JsonProperty("password") PasswordDTO password,
                                  @Valid @NotNull @JsonProperty("email") EmailDTO email,
                                  @Nullable @JsonProperty("tracing_id") String tracingId) {

    public RegisterUserRequest withRequestId(UUID requestId) {
        return new RegisterUserRequest(requestId, password, email, tracingId);
    }

    public RegisterUserRequest withPassword(PasswordDTO password) {
        return new RegisterUserRequest(requestId, password, email, tracingId);
    }

    public RegisterUserRequest withEmail(EmailDTO email) {
        return new RegisterUserRequest(requestId, password, email, tracingId);
    }

    public RegisterUserRequest withTracingId(String tracingId) {
        return new RegisterUserRequest(requestId, password, email, tracingId);
    }
}