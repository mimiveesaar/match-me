package tech.kood.match_me.user_management.features.refreshToken.features.getToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;

import java.util.UUID;

public record GetRefreshTokenRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                     @NotNull @Valid @JsonProperty("shared_secret") RefreshTokenSecretDTO secret,
                                     @Nullable @JsonProperty("tracing_id") String tracingId) {

    public GetRefreshTokenRequest(RefreshTokenSecretDTO secret, String tracingId) {
        this(UUID.randomUUID(), secret, tracingId);
    }

    public GetRefreshTokenRequest withRequestId(UUID requestId) {
        return new GetRefreshTokenRequest(requestId, secret, tracingId);
    }

    public GetRefreshTokenRequest withToken(RefreshTokenSecretDTO secret) {
        return new GetRefreshTokenRequest(requestId, secret, tracingId);
    }

    public GetRefreshTokenRequest withTracingId(String tracingId) {
        return new GetRefreshTokenRequest(requestId, secret, tracingId);
    }
}