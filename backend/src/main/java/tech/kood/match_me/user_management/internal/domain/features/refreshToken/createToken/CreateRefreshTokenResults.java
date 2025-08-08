package tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.domain.models.RefreshToken;

public sealed interface CreateRefreshTokenResults extends Result
                permits CreateRefreshTokenResults.Success, CreateRefreshTokenResults.UserNotFound,
                CreateRefreshTokenResults.SystemError {

        final class Success implements CreateRefreshTokenResults {

                @NotNull
                private final UUID requestId;

                @NotNull
                private final RefreshToken refreshToken;

                @Nullable
                private final String tracingId;


                private Success(RefreshToken refreshToken, UUID requestId,
                                @Nullable String tracingId) {
                        this.refreshToken = refreshToken;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonProperty("refreshToken")
                public RefreshToken getRefreshToken() {
                        return refreshToken;
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
                public static Success of(
                                @JsonProperty("refreshToken") @NotNull RefreshToken refreshToken,
                                @JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var success = new Success(refreshToken, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance.validate(success);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return success;
                }
        }

        final class UserNotFound implements CreateRefreshTokenResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String userId;

                @Nullable
                private final String tracingId;


                private UserNotFound(String userId, UUID requestId, @Nullable String tracingId) {
                        this.userId = userId;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonProperty("userId")
                public String getUserId() {
                        return userId;
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
                public static UserNotFound of(@JsonProperty("userId") @NotEmpty String userId,
                                @JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var userNotFound = new UserNotFound(userId, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance.validate(userNotFound);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return userNotFound;
                }
        }

        final class SystemError implements CreateRefreshTokenResults {

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
