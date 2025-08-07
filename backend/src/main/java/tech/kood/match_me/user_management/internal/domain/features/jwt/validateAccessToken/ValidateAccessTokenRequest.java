package tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken;

import jakarta.annotation.Nullable;
import java.io.Serializable;


public record ValidateAccessTokenRequest(String requestID, String jwtToken,
        @Nullable String tracingId) implements Serializable {
}
