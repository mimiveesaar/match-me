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
                @JsonProperty("requestId")
                public final UUID requestId;

                @NotNull
                @JsonProperty("token")
                public final RefreshToken token;

                @Nullable
                @JsonProperty("tracingId")
                public final String tracingId;

                private Success(RefreshToken token, UUID requestId, @Nullable String tracingId) {
                        this.token = token;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
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
