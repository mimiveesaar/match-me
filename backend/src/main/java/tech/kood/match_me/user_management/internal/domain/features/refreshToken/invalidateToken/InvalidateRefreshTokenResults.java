package tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

public sealed interface InvalidateRefreshTokenResults extends Result
                permits InvalidateRefreshTokenResults.Success, InvalidateRefreshTokenResults.TokenNotFound,
                InvalidateRefreshTokenResults.InvalidRequest, InvalidateRefreshTokenResults.SystemError {

        final class Success implements InvalidateRefreshTokenResults {

                @NotNull
                @JsonProperty("requestId")
                public final UUID requestId;

                @Nullable
                @JsonProperty("tracingId")
                public final String tracingId;

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
                @JsonProperty("requestId")
                public final UUID requestId;

                @NotEmpty
                @JsonProperty("token")
                public final String token;

                @Nullable
                @JsonProperty("tracingId")
                public final String tracingId;

                private TokenNotFound(String token, UUID requestId, @Nullable String tracingId) {
                        this.token = token;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static TokenNotFound of(@JsonProperty("token") @NotEmpty String token,
                                @JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var tokenNotFound = new TokenNotFound(token, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance.validate(tokenNotFound);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return tokenNotFound;
                }
        }

        final class InvalidRequest implements InvalidateRefreshTokenResults {

                @NotNull
                @JsonProperty("requestId")
                public final UUID requestId;

                @NotEmpty
                @JsonProperty("message")
                public final String message;

                @Nullable
                @JsonProperty("tracingId")
                public final String tracingId;

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
                        var violations = DomainObjectInputValidator.instance.validate(invalidRequest);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return invalidRequest;
                }
        }

        final class SystemError implements InvalidateRefreshTokenResults {

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
