package tech.kood.match_me.user_management.internal.features.user.registerUser;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Result;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;

public sealed interface RegisterUserResults
                permits RegisterUserResults.Success, RegisterUserResults.UsernameExists,
                RegisterUserResults.EmailExists, RegisterUserResults.InvalidPassword,
                RegisterUserResults.InvalidUsername, RegisterUserResults.SystemError {

    record Success(@NotNull User user, @NotNull UUID requestId,
                   @Nullable String tracingId) implements RegisterUserResults {

        @Override
        @JsonProperty("user")
        public User user() {
            return user;
        }

        @Override
        @JsonProperty("requestId")
        public UUID requestId() {
            return requestId;
        }

        @Override
        @JsonProperty("tracingId")
        public String tracingId() {
            return tracingId;
        }

        public Success(User user, UUID requestId, @Nullable String tracingId) {
            this.user = user;
            this.requestId = requestId;
            this.tracingId = tracingId;
        }

        @JsonCreator
        public static Success of(@JsonProperty("requestId") @NotNull UUID requestId,
                                 @JsonProperty("user") @NotNull User user,
                                 @JsonProperty("tracingId") @Nullable String tracingId) {
            var success = new Success(user, requestId, tracingId);
            var violations = DomainObjectInputValidator.instance.validate(success);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(
                        violations);
            }
            return success;
        }
    }

        final class UsernameExists implements RegisterUserResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String username;

                @Nullable
                private final String tracingId;

                @JsonProperty("username")
                public String getUsername() {
                        return username;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                public String getTracingId() {
                        return tracingId;
                }

                private UsernameExists(String username, UUID requestId,
                                @Nullable String tracingId) {
                        this.username = username;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static UsernameExists of(@JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("username") @NotEmpty String username,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var usernameExists = new UsernameExists(username, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance
                                        .validate(usernameExists);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return usernameExists;
                }
        }

        final class EmailExists implements RegisterUserResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String email;

                @Nullable
                private final String tracingId;

                @JsonProperty("email")
                public String getEmail() {
                        return email;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                public String getTracingId() {
                        return tracingId;
                }

                private EmailExists(String email, UUID requestId, @Nullable String tracingId) {
                        this.email = email;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static EmailExists of(@JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("email") @NotEmpty String email,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var emailExists = new EmailExists(email, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance.validate(emailExists);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return emailExists;
                }
        }


        enum InvalidUsernameType {
                TOO_SHORT, TOO_LONG, INVALID_CHARACTERS
        }

        final class InvalidUsername implements RegisterUserResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String username;

                @NotNull
                private final InvalidUsernameType type;

                @Nullable
                private final String tracingId;

                @JsonProperty("username")
                public String getUsername() {
                        return username;
                }

                @JsonProperty("type")
                public InvalidUsernameType getType() {
                        return type;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                public String getTracingId() {
                        return tracingId;
                }

                private InvalidUsername(String username, InvalidUsernameType type, UUID requestId,
                                @Nullable String tracingId) {
                        this.username = username;
                        this.type = type;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static InvalidUsername of(@JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("username") @NotEmpty String username,
                                @JsonProperty("type") @NotNull InvalidUsernameType type,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var invalidUsername =
                                        new InvalidUsername(username, type, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance
                                        .validate(invalidUsername);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return invalidUsername;
                }
        }

        enum InvalidPasswordType {
                TOO_SHORT, TOO_LONG, WEAK
        }

        final class InvalidPassword implements RegisterUserResults {

                @NotNull
                private final UUID requestId;

                @NotEmpty
                private final String password;

                @NotNull
                private final InvalidPasswordType type;

                @Nullable
                private final String tracingId;

                @JsonProperty("password")
                public String getPassword() {
                        return password;
                }

                @JsonProperty("type")
                public InvalidPasswordType getType() {
                        return type;
                }

                @JsonProperty("requestId")
                public UUID getRequestId() {
                        return requestId;
                }

                @JsonProperty("tracingId")
                public String getTracingId() {
                        return tracingId;
                }

                private InvalidPassword(String password, InvalidPasswordType type, UUID requestId,
                                @Nullable String tracingId) {
                        this.password = password;
                        this.type = type;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static InvalidPassword of(@JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("password") @NotEmpty String password,
                                @JsonProperty("type") @NotNull InvalidPasswordType type,
                                @JsonProperty("tracingId") @Nullable String tracingId) {
                        var invalidPassword =
                                        new InvalidPassword(password, type, requestId, tracingId);
                        var violations = DomainObjectInputValidator.instance
                                        .validate(invalidPassword);
                        if (!violations.isEmpty()) {
                                throw new jakarta.validation.ConstraintViolationException(
                                                violations);
                        }
                        return invalidPassword;
                }
        }

        final class SystemError implements RegisterUserResults {

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
                public String getTracingId() {
                        return tracingId;
                }

                private SystemError(String message, UUID requestId, @Nullable String tracingId) {
                        this.message = message;
                        this.requestId = requestId;
                        this.tracingId = tracingId;
                }

                @JsonCreator
                public static SystemError of(@JsonProperty("requestId") @NotNull UUID requestId,
                                @JsonProperty("message") @NotEmpty String message,
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
