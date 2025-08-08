package tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.domain.models.RefreshToken;

public sealed interface GetRefreshTokenResults extends Result
                permits GetRefreshTokenResults.Success, GetRefreshTokenResults.InvalidToken,
                GetRefreshTokenResults.SystemError {

        final class Success implements GetRefreshTokenResults {

                @NotNull
                private final UUID requestId;

                @NotNull
                private final RefreshToken token;

                @Nullable
                private final String tracingId;


                private Success(RefreshToken token, UUID requestId, @Nullable String tracingId) {
                        this.token = token;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonProperty("token")
                public RefreshToken getToken() {
                        return token;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                public String getTracingId() {
                        return tracingId;
                }

                @JsonCreator
                public static Success of(@JsonProperty("token") @NotNull RefreshToken token,
                                @JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var success = new Success(token, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance.validate(success);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return success;
                }
        }

        final class InvalidToken implements GetRefreshTokenResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String token;

                @Nullable
                private final String tracingId;


                private InvalidToken(String token, UUID requestId, @Nullable String tracingId) {
                        this.token = token;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonProperty("token")
                public String getToken() {
                        return token;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                public String getTracingId() {
                        return tracingId;
                }

                @JsonCreator
                public static InvalidToken of(@JsonProperty("token") @NotEmpty String token,
                                @JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var invalidToken = new InvalidToken(token, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance.validate(invalidToken);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return invalidToken;
                }
        }

        final class SystemError implements GetRefreshTokenResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String message;

                @Nullable
                private final String tracingId;


                private SystemError(String message, UUID requestId, @Nullable String tracingId) {
                        this.message = message;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonProperty("message")
                public String getMessage() {
                        return message;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                public String getTracingId() {
                        return tracingId;
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
