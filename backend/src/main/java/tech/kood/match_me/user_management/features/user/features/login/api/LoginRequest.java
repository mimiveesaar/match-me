package tech.kood.match_me.user_management.features.user.features.login.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;


@Command
public record LoginRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                           @NotEmpty @Valid @JsonProperty("email") EmailDTO email,
                           @NotEmpty @Valid @JsonProperty("password") PasswordDTO password,
                           @Nullable @JsonProperty("tracing_id") String tracingId) {


    public LoginRequest withEmail(LoginRequest request, EmailDTO email) {
        return new LoginRequest(request.requestId, email, request.password, request.tracingId);
    }

    public LoginRequest withPassword(LoginRequest request, PasswordDTO password) {
        return new LoginRequest(request.requestId, request.email, password, request.tracingId);
    }

    public LoginRequest withTracingId(LoginRequest request, String tracingId) {
        return new LoginRequest(request.requestId, request.email, request.password, tracingId);
    }
}
