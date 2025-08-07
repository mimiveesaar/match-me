
package tech.kood.match_me.user_management.internal.domain.features.jwt.getAccessToken;

import java.io.Serializable;

public record GetAccessTokenRequest(String requestId, String refreshToken, String tracingId)
        implements Serializable {

    public GetAccessTokenRequest(String refreshToken, String tracingId) {
        this(java.util.UUID.randomUUID().toString(), refreshToken, tracingId);
    }

}
