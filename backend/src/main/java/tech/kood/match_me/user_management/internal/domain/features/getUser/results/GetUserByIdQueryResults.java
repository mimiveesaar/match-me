package tech.kood.match_me.user_management.internal.domain.features.getUser.results;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.domain.models.User;
import tech.kood.match_me.user_management.internal.domain.models.UserId;

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
        @JsonProperty("requestId")
        public final UUID requestId;

        @NotNull
        @JsonProperty("user")
        public final User user;

        @Nullable
        @JsonProperty("tracingId")
        public final String tracingId;

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
        @JsonProperty("requestId")
        public final UUID requestId;

        @NotNull
        @JsonProperty("userId")
        public final UserId id;

        @Nullable
        @JsonProperty("tracingId")
        public final String tracingId;

        private UserNotFound(UserId id, UUID requestId, @Nullable String tracingId) {
            this.id = id;
            this.requestId = requestId;
            this.tracingId = tracingId;
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
        public final String message;

        @NotNull
        public final UUID requestId;

        @Nullable
        public final String tracingId;

        private SystemError(String message, UUID requestId, @Nullable String tracingId) {
            this.message = message;
            this.requestId = requestId;
            this.tracingId = tracingId;
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
