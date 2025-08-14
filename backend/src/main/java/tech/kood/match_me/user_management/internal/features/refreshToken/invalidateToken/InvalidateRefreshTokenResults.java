package tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

public sealed interface InvalidateRefreshTokenResults extends Result permits
                InvalidateRefreshTokenResults.Success, InvalidateRefreshTokenResults.TokenNotFound,
                InvalidateRefreshTokenResults.InvalidRequest,
                InvalidateRefreshTokenResults.SystemError {

        final class Success implements InvalidateRefreshTokenResults {

                @NotNull
                private final UUID requestId;

                @Nullable
                private final String tracingId;

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                @Nullable
                public String getTracingId() {
                        return tracingId;
                }

                private Success(UUID requestId, @Nullable String tracingId) {
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static Success of(@JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var success = new Success(requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance.validate(success);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return success;
                }
        }

        final class TokenNotFound implements InvalidateRefreshTokenResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String refreshToken;

                @Nullable
                private final String tracingId;

                @JsonProperty("refreshToken")
                public String getRefreshToken() {
                        return refreshToken;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                @Nullable
                public String getTracingId() {
                        return tracingId;
                }

                private TokenNotFound(String token, UUID requestId, @Nullable String tracingId) {
                        this.refreshToken = token;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static TokenNotFound of(
                                @JsonProperty("refreshToken") @NotEmpty String refreshToken,
                                @JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var tokenNotFound = new TokenNotFound(refreshToken, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance
                                        .validate(tokenNotFound);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return tokenNotFound;
                }
        }

        final class InvalidRequest implements InvalidateRefreshTokenResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String message;

                @Nullable
                private final String tracingId;

                @JsonProperty("message")
                public String getMessage() {
                        return message;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                @Nullable
                public String getTracingId() {
                        return tracingId;
                }

                private InvalidRequest(String message, UUID requestId, @Nullable String tracingId) {
                        this.message = message;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static InvalidRequest of(@JsonProperty("message") @NotEmpty String message,
                                @JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var invalidRequest = new InvalidRequest(message, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance
                                        .validate(invalidRequest);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return invalidRequest;
                }
        }

        final class SystemError implements InvalidateRefreshTokenResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String message;

                @Nullable
                private final String tracingId;

                @JsonProperty("message")
                public String getMessage() {
                        return message;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                @Nullable
                public String getTracingId() {
                        return tracingId;
                }

                private SystemError(String message, UUID requestId, @Nullable String tracingId) {
                        this.message = message;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static SystemError of(@JsonProperty("message") @NotEmpty String message,
                                @JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var systemError = new SystemError(message, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance.validate(systemError);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return systemError;
                }
        }
}
