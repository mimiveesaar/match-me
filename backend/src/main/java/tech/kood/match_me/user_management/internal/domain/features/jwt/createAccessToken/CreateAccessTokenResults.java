package tech.kood.match_me.user_management.internal.domain.features.jwt.createAccessToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

public sealed interface CreateAccessTokenResults extends Result
        permits CreateAccessTokenResults.Success, CreateAccessTokenResults.InvalidToken,
        CreateAccessTokenResults.SystemError {

    /**
     * Represents a successful result of fetching an access token. Usually by CQRS principles we
     * shouldn't return a result (except ID) from a command, but JWT kinda counts as an ID.
     *
     * @param jwt The JWT access token.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class Success implements CreateAccessTokenResults {

        @NotNull
        @JsonProperty("requestId")
        public final UUID requestId;

        @NotEmpty
        @JsonProperty("jwt")
        public final String jwt;

        @Nullable
        @JsonProperty("tracingId")
        public final String tracingId;

        private Success(String jwt, UUID requestId, String tracingId) {
            this.jwt = jwt;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonCreator
        public static Success of(@JsonProperty("jwt") String jwt,
                @JsonProperty("requestId") UUID requestId,
                @JsonProperty("tracingId") @Nullable String tracingId) {
            var success = new Success(jwt, requestId, tracingId);

            var violations = DomainObjectInputValidator.instance.validate(success);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }
            return success;
        }
    }

    /**
     * Represents the result when a refresh token is invalid.
     *
     * @param token The invalid refresh token.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class InvalidToken implements CreateAccessTokenResults {

        @NotNull
        @JsonProperty("requestId")
        public final UUID requestId;

        @NotEmpty
        @JsonProperty("token")
        public final String token;

        @Nullable
        @JsonProperty("tracingId")
        public final String tracingId;

        private InvalidToken(String token, UUID requestId, @Nullable String tracingId) {
            this.token = token;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonCreator
        public static InvalidToken of(@JsonProperty("token") @NotNull String token,
                @JsonProperty("requestId") @NotNull UUID requestId,
                @JsonProperty("tracingId") @Nullable String tracingId) {
            var invalidToken = new InvalidToken(token, requestId, tracingId);

            var violations = DomainObjectInputValidator.instance.validate(invalidToken);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }

            return invalidToken;
        }
    }

    /**
     * Represents a system error result for the GetAccessToken operation.
     *
     * @param message The error message describing the system error.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class SystemError implements CreateAccessTokenResults {

        @NotNull
        @JsonProperty("requestId")
        public final UUID requestId;

        @NotEmpty
        @JsonProperty("message")
        public final String message;

        @Nullable
        @JsonProperty("tracingId")
        public final String tracingId;

        private SystemError(String message, UUID requestId, @Nullable String tracingId) {
            this.message = message;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonCreator
        public static SystemError of(@JsonProperty("message") @NotNull String message,
                @JsonProperty("requestId") @NotNull UUID requestId,
                @JsonProperty("tracingId") @Nullable String tracingId) {
            var systemError = new SystemError(message, requestId, tracingId);

            var violations = DomainObjectInputValidator.instance.validate(systemError);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }
            return systemError;
        }
    }
}
