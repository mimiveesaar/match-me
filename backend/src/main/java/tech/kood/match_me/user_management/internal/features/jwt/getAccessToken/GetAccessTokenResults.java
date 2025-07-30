
package tech.kood.match_me.user_management.internal.features.jwt.getAccessToken;

import java.io.Serializable;
import jakarta.annotation.Nullable;

public sealed interface GetAccessTokenResults extends Serializable {

    record Success(String jwt, @Nullable String tracingId) implements GetAccessTokenResults {
    }

    record InvalidToken(String token, @Nullable String tracingId) implements GetAccessTokenResults {
    }

    record InvalidRequest(String message, @Nullable String tracingId)
            implements GetAccessTokenResults {
    }

    record SystemError(String message, @Nullable String tracingId)
            implements GetAccessTokenResults {
    }
}
