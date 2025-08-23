package tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;

@Command
public record CreateAccessTokenRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                       @NotNull @Valid @JsonProperty("secret") RefreshTokenSecretDTO secret,
                                       @Nullable @JsonProperty("tracing_id") String tracingId) {

    public CreateAccessTokenRequest(RefreshTokenSecretDTO secret, @Nullable String tracingId) {
        this(UUID.randomUUID(), secret, tracingId);
    }

    public CreateAccessTokenRequest withRequestId(UUID requestId) {
        return new CreateAccessTokenRequest(requestId, secret, tracingId);
    }

    public CreateAccessTokenRequest withSecret(RefreshTokenSecretDTO secret) {
        return new CreateAccessTokenRequest(requestId, secret, tracingId);
    }

    public CreateAccessTokenRequest withTracingId(String tracingId) {
        return new CreateAccessTokenRequest(requestId, secret, tracingId);
    }
}