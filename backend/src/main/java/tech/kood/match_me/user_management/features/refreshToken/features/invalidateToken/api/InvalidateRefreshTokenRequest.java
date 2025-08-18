package tech.kood.match_me.user_management.features.refreshToken.features.invalidateToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.SharedSecretDTO;


@Command
public record InvalidateRefreshTokenRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                            @NotNull @Valid @JsonProperty("secret") SharedSecretDTO secret,
                                            @Nullable @JsonProperty("tracing_id") String tracingId) {

    public InvalidateRefreshTokenRequest withRequestId(UUID requestId) {
        return new InvalidateRefreshTokenRequest(requestId, secret, tracingId);
    }

    public InvalidateRefreshTokenRequest withSecret(SharedSecretDTO secret) {
        return new InvalidateRefreshTokenRequest(requestId, secret, tracingId);
    }

    public InvalidateRefreshTokenRequest withTracingId(String tracingId) {
        return new InvalidateRefreshTokenRequest(requestId, secret, tracingId);
    }
}
