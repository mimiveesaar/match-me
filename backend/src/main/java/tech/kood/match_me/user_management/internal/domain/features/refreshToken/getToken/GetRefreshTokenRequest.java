package tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken;

import java.util.UUID;
import jakarta.annotation.Nullable;

public record GetRefreshTokenRequest(String requestID, String token, @Nullable String tracingId) {

    public GetRefreshTokenRequest(String token, @Nullable String tracingId) {
        this(UUID.randomUUID().toString(), token, tracingId);
    }
}
