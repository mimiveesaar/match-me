package tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken;

import java.util.Optional;
import java.util.UUID;

public record ValidateAccessTokenRequest(
        UUID requestID,
        String jwtToken,
        Optional<String> tracingId) {

    public ValidateAccessTokenRequest(String jwtToken, Optional<String> tracingId) {
        this(UUID.randomUUID(), jwtToken, tracingId);
    }
}