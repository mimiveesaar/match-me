package tech.kood.match_me.user_management.internal.features.login;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.domain.models.RefreshToken;
import tech.kood.match_me.user_management.internal.features.user.User;

public sealed interface LoginResults extends Result
        permits LoginResults.Success, LoginResults.InvalidCredentials, LoginResults.SystemError {

    final class Success implements LoginResults {

        @NotNull
        private final UUID requestId;

        @NotNull
        private final RefreshToken refreshToken;

        @NotNull
        private final User user;

        @Nullable
        private final String tracingId;

        @JsonProperty("refreshToken")
        public RefreshToken getRefreshToken() {
            return refreshToken;
        }

        @JsonProperty("user")
        public User getUser() {
            return user;
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

        private Success(RefreshToken refreshToken, User user, UUID requestId,
                @Nullable String tracingId) {
            this.refreshToken = refreshToken;
            this.user = user;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonCreator
        public static Success of(@JsonProperty("refreshToken") @NotNull RefreshToken refreshToken,
                @JsonProperty("user") @NotNull User user,
                @JsonProperty("requestId") @NotNull UUID requestId,
                @JsonProperty("tracingId") @Nullable String tracingId) {
            var success = new Success(refreshToken, user, requestId, tracingId);
            var violations = DomainObjectInputValidator.instance.validate(success);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }
            return success;
        }

    }

    final class InvalidCredentials implements LoginResults {

        @NotNull
        private final UUID requestId;

        @NotEmpty
        private final String email;

        @NotEmpty
        private final String password;

        @Nullable
        private final String tracingId;

        @JsonProperty("email")
        public String getEmail() {
            return email;
        }

        @JsonProperty("password")
        public String getPassword() {
            return password;
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

        private InvalidCredentials(String email, String password, UUID requestId,
                @Nullable String tracingId) {
            this.email = email;
            this.password = password;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonCreator
        public static InvalidCredentials of(@JsonProperty("email") @NotEmpty String email,
                @JsonProperty("password") @NotEmpty String password,
                @JsonProperty("requestId") @NotNull UUID requestId,
                @JsonProperty("tracingId") @Nullable String tracingId) {
            var invalidCreds = new InvalidCredentials(email, password, requestId, tracingId);
            var violations = DomainObjectInputValidator.instance.validate(invalidCreds);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }
            return invalidCreds;
        }
    }

    final class SystemError implements LoginResults {

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
                throw new jakarta.validation.ConstraintViolationException(violations);
            }
            return systemError;
        }
    }
}
