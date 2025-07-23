package tech.kood.match_me.user_management.internal.features.jwt;

import java.util.Optional;
import java.util.UUID;

public record GetAccessTokenRequest(UUID requestId, String refreshToken, Optional<String> tracingId) {

    public GetAccessTokenRequest(String refreshToken, Optional<String> tracingId) {
        this(UUID.randomUUID(), refreshToken, tracingId);
    }

}