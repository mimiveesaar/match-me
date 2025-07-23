package tech.kood.match_me.user_management.internal.features.jwt.getAccessToken;

import java.util.Optional;

public sealed interface GetAccessTokenResults {

    record Success(String jwt, Optional<String> tracingId) implements GetAccessTokenResults {
    }

    record InvalidToken(String token, Optional<String> tracingId) implements GetAccessTokenResults {
    }

    record InvalidRequest(String message, Optional<String> tracingId) implements GetAccessTokenResults {
    }

    record SystemError(String message, Optional<String> tracingId) implements GetAccessTokenResults {
    }
}