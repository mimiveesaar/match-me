
package tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken;


import jakarta.annotation.Nullable;
import tech.kood.match_me.user_management.internal.domain.models.AccessToken;

public sealed interface ValidateAccessTokenResults permits ValidateAccessTokenResults.Success,
                ValidateAccessTokenResults.InvalidToken, ValidateAccessTokenResults.InvalidRequest {

        record Success(AccessToken accessToken, @Nullable String tracingId)
                        implements ValidateAccessTokenResults {
        }

        record InvalidToken(String jwt, @Nullable String tracingId)
                        implements ValidateAccessTokenResults {
        }

        record InvalidRequest(String message, @Nullable String tracingId)
                        implements ValidateAccessTokenResults {
        }
}
