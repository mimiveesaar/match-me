
package tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.domain.models.AccessToken;

public sealed interface ValidateAccessTokenResults extends Result permits
                ValidateAccessTokenResults.Success, ValidateAccessTokenResults.InvalidToken {

        /**
         * Represents a successful result of validating an access token.
         *
         * @param accessToken The validated access token.
         * @param requestId The unique internal identifier for the request.
         * @param tracingId An optional tracing identifier for external request tracking.
         */
        final class Success implements ValidateAccessTokenResults {

                @NotNull
                private final UUID requestId;

                @NotNull
                private final AccessToken accessToken;

                @Nullable
                private final String tracingId;

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("accessToken")
                public AccessToken getAccessToken() {
                        return accessToken;
                }

                @JsonProperty("tracingId")
                public String getTracingId() {
                        return tracingId;
                }

                private Success(AccessToken accessToken, UUID requestId, String tracingId) {
                        this.accessToken = accessToken;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static Success of(@JsonProperty("accessToken") AccessToken accessToken,
                                @JsonProperty("requestId") UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var success = new Success(accessToken, requestId, tracingId);

                        var violations = DomainObjectInputValidator.instance.validate(success);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return success;
                }
        }

        /**
         * Represents the result when an access token is invalid.
         *
         * @param jwt The invalid JWT token.
         * @param requestId The unique internal identifier for the request.
         * @param tracingId An optional tracing identifier for external request tracking.
         */
        final class InvalidToken implements ValidateAccessTokenResults {

                @NotNull
                private final UUID requestId;

                @NotNull
                private final String jwt;

                @Nullable
                private final String tracingId;

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("jwt")
                public String getJwt() {
                        return jwt;
                }

                @JsonProperty("tracingId")
                public String getTracingId() {
                        return tracingId;
                }

                private InvalidToken(String jwt, UUID requestId, @Nullable String tracingId) {
                        this.jwt = jwt;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static InvalidToken of(@JsonProperty("jwt") @NotNull String jwt,
                                @JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var invalidToken = new InvalidToken(jwt, requestId, tracingId);

                        var violations = DomainObjectInputValidator.instance.validate(invalidToken);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }

                        return invalidToken;
                }
        }

}
