package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;

public sealed interface GetUserByUsernameQueryResults extends Result
        permits GetUserByUsernameQueryResults.Success, GetUserByUsernameQueryResults.UserNotFound,
        GetUserByUsernameQueryResults.SystemError {

    /**
     * Represents a successful result of fetching a user by username.
     *
     * @param user The {@link User} object containing user details.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class Success implements GetUserByUsernameQueryResults {

        @NotNull
        private final UUID requestId;

        @NotNull
        private final User user;

        @Nullable
        private final String tracingId;

        @JsonProperty("requestId")
        @NotNull
        public UUID getRequestId() {
            return requestId;
        }

        @JsonProperty("user")
        @NotNull
        public User getUser() {
            return user;
        }

        @JsonProperty("tracingId")
        @Nullable
        public String getTracingId() {
            return tracingId;
        }

        private Success(User user, UUID requestId, String tracingId) {
            this.user = user;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonCreator
        public static Success of(@JsonProperty("user") User user,
                @JsonProperty("requestId") UUID requestId,
                @JsonProperty("tracingId") @Nullable String tracingId) {
            var success = new Success(user, requestId, tracingId);

            var violations = DomainObjectInputValidator.instance.validate(success);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }
            return success;
        }
    }

    /**
     * Represents the result when a user is not found by their username.
     *
     * @param username The username of the user that was not found.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class UserNotFound implements GetUserByUsernameQueryResults {

        @NotNull
        private final UUID requestId;

        @NotNull
        private final String username;

        @Nullable
        private final String tracingId;

        @JsonProperty("requestId")
        @NotNull
        public UUID getRequestId() {
            return requestId;
        }

        @JsonProperty("username")
        @NotNull
        public String getUsername() {
            return username;
        }

        @JsonProperty("tracingId")
        @Nullable
        public String getTracingId() {
            return tracingId;
        }

        private UserNotFound(String username, UUID requestId, @Nullable String tracingId) {
            this.username = username;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonCreator
        public static UserNotFound of(@JsonProperty("username") @NotNull String username,
                @JsonProperty("requestId") @NotNull UUID requestId,
                @JsonProperty("tracingId") @Nullable String tracingId) {
            var userNotFound = new UserNotFound(username, requestId, tracingId);

            var violations = DomainObjectInputValidator.instance.validate(userNotFound);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }

            return userNotFound;
        }
    }

    /**
     * Represents a system error result for the GetUserByUsername operation.
     *
     * @param message The error message describing the system error.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class SystemError implements GetUserByUsernameQueryResults {

        @NotNull
        private final UUID requestId;

        @NotNull
        private final String message;

        @Nullable
        private final String tracingId;

        @JsonProperty("requestId")
        @NotNull
        public UUID getRequestId() {
            return requestId;
        }

        @JsonProperty("message")
        @NotNull
        public String getMessage() {
            return message;
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
