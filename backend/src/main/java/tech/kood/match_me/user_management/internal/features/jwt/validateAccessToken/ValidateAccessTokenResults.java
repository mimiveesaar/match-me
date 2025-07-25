package tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken;

import java.util.Optional;

import tech.kood.match_me.user_management.models.AccessToken;

public sealed interface ValidateAccessTokenResults permits ValidateAccessTokenResults.Success,
        ValidateAccessTokenResults.InvalidToken, ValidateAccessTokenResults.InvalidRequest {

    record Success(AccessToken accessToken, Optional<String> tracingId)
            implements ValidateAccessTokenResults {
    }

    record InvalidToken(String jwt, Optional<String> tracingId)
            implements ValidateAccessTokenResults {
    }

    record InvalidRequest(String message, Optional<String> tracingId)
            implements ValidateAccessTokenResults {
    }
}
