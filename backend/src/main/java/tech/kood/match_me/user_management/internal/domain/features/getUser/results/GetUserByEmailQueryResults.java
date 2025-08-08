package tech.kood.match_me.user_management.internal.domain.features.getUser.results;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.domain.models.User;


public sealed interface GetUserByEmailQueryResults extends Result
        permits GetUserByEmailQueryResults.Success, GetUserByEmailQueryResults.UserNotFound,
        GetUserByEmailQueryResults.SystemError {


    /**
     * Represents a successful result of the GetUserByEmail query.
     * <p>
     * This class encapsulates the details of a successful user retrieval operation, including the
     * user information, the request identifier, and an optional tracing ID.
     * </p>
     *
     * <p>
     * Instances of this class are immutable and can only be created via the static
     * {@link #of(User, UUID, String)} factory method, which also performs validation using
     * {@link DomainObjectInputValidator}.
     * </p>
     *
     * @implNote This class is package-private and intended for internal use within the user
     *           management domain.
     */
    final class Success implements GetUserByEmailQueryResults {

        @NotNull
        private final UUID requestId;

        @NotNull
        private final User user;

        @Nullable
        private final String tracingId;

        @JsonProperty("requestId")
        @Nonnull
        public UUID getRequestId() {
            return requestId;
        }

        @JsonProperty("user")
        @Nonnull
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
     * Represents the result when a user is not found by their email.
     *
     * @param email The email of the user that was not found.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class UserNotFound implements GetUserByEmailQueryResults {

        @NotNull
        private final UUID requestId;

        @NotNull
        @Email
        private final String email;

        @Nullable
        private final String tracingId;

        @JsonProperty("requestId")
        @Nonnull
        public UUID getRequestId() {
            return requestId;
        }

        @JsonProperty("email")
        @Nonnull
        public String getEmail() {
            return email;
        }

        @JsonProperty("tracingId")
        @Nullable
        public String getTracingId() {
            return tracingId;
        }

        private UserNotFound(String email, UUID requestId, @Nullable String tracingId) {
            this.email = email;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonCreator
        public static UserNotFound of(@JsonProperty("email") @NotEmpty String email,
                @JsonProperty("requestId") @NotNull UUID requestId,
                @JsonProperty("tracingId") @Nullable String tracingId) {
            var userNotFound = new UserNotFound(email, requestId, tracingId);

            var violations = DomainObjectInputValidator.instance.validate(userNotFound);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }

            return userNotFound;
        }
    }

    /**
     * Represents a system error result for the GetUserByEmail operation.
     *
     * @param message The error message describing the system error.
     * @param requestId The unique internal identifier for the request.
     * @param tracingId An optional tracing identifier for external request tracking.
     */
    final class SystemError implements GetUserByEmailQueryResults {

        @NotNull
        private final String message;

        @NotNull
        private final UUID requestId;

        @Nullable
        private final String tracingId;

        @JsonProperty("message")
        @Nonnull
        public String getMessage() {
            return message;
        }

        @JsonProperty("requestId")
        @Nonnull
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

        public static SystemError of(@NotEmpty String message, @NotNull UUID requestId,
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
