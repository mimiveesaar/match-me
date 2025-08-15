package tech.kood.match_me.user_management.internal.features.getUser.results;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;

public sealed interface GetUserByIdQueryResults extends Result
        permits GetUserByIdQueryResults.Success, GetUserByIdQueryResults.UserNotFound,
        GetUserByIdQueryResults.SystemError {

    /**
     * Represents a successful result of fetching a user by ID.
     *
     * @param user The {@link User} object containing user details.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class Success implements GetUserByIdQueryResults {

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
     * Represents the result when a user is not found by their ID.
     *
     * @param id The UUID of the user that was not found.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class UserNotFound implements GetUserByIdQueryResults {

        @NotNull
        private final UUID requestId;

        @NotNull
        private final UserId id;

        @Nullable
        private final String tracingId;

        private UserNotFound(UserId id, UUID requestId, @Nullable String tracingId) {
            this.id = id;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonProperty("requestId")
        @NotNull
        public UUID getRequestId() {
            return requestId;
        }

        @JsonProperty("userId")
        @NotNull
        public UserId getId() {
            return id;
        }

        @JsonProperty("tracingId")
        @Nullable
        public String getTracingId() {
            return tracingId;
        }

        @JsonCreator
        public static UserNotFound of(@JsonProperty("userId") @NotNull UserId id,
                @JsonProperty("requestId") @NotNull UUID requestId,
                @JsonProperty("tracingId") @Nullable String tracingId) {
            var userNotFound = new UserNotFound(id, requestId, tracingId);

            var violations = DomainObjectInputValidator.instance.validate(userNotFound);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }

            return userNotFound;
        }
    }

    /**
     * Represents a system error result for the GetUserById operation.
     *
     * @param message The error message describing the system error.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class SystemError implements GetUserByIdQueryResults {

        @NotNull
        private final String message;

        @NotNull
        private final UUID requestId;

        @Nullable
        private final String tracingId;

        private SystemError(String message, UUID requestId, @Nullable String tracingId) {
            this.message = message;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonProperty("message")
        @NotNull
        public String getMessage() {
            return message;
        }

        @JsonProperty("requestId")
        @NotNull
        public UUID getRequestId() {
            return requestId;
        }

        @JsonProperty("tracingId")
        @Nullable
        public String getTracingId() {
            return tracingId;
        }

        @JsonCreator
        public static SystemError of(@NotNull String message, @NotNull UUID requestId,
                @Nullable String tracingId) {
            var systemError = new SystemError(message, requestId, tracingId);

            var violations = DomainObjectInputValidator.instance.validate(systemError);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }
            return systemError;
        }
    }
}
