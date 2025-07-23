package tech.kood.match_me.user_management.internal.features.refreshToken.getToken;

import java.util.Optional;
import java.util.UUID;

public record GetRefreshTokenRequest(
        UUID requestID,
        String token,
        Optional<String> tracingId) {

    public GetRefreshTokenRequest(String token, Optional<String> tracingId) {
        this(UUID.randomUUID(), token, tracingId);
    }
}