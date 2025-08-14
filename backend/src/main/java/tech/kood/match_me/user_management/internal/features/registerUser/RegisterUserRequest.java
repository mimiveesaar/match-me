package tech.kood.match_me.user_management.internal.features.registerUser;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Query;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

/**
 * Represents a request to register a new user in the system.
 *
 * <ul>
 * <li>{@code requestId} - Unique identifier for the registration request (must not be null).</li>
 * <li>{@code username} - The username of the user to be registered (must not be blank).</li>
 * <li>{@code password} - The password for the new user (must not be blank).</li>
 * <li>{@code email} - The email address of the user (must not be blank and must be valid).</li>
 * <li>{@code tracingId} - Optional tracing identifier for request tracking (nullable).</li>
 * </ul>
 *
 * <p>
 * Use the static factory method {@link #of(UUID, String, String, String, String)} to create
 * instances, which performs validation. If validation fails, a {@link ConstraintViolationException}
 * is thrown.
 * </p>
 *
 * <p>
 * Provides "with" methods to create modified copies of the request with updated fields.
 * </p>
 */
public final class RegisterUserRequest implements Query {

    @NotNull
    private final UUID requestId;

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    @Email
    @NotBlank
    private final String email;

    @Nullable
    private final String tracingId;

    @JsonProperty("requestId")
    @Nonnull
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty("username")
    @Nonnull
    public String getUsername() {
        return username;
    }

    @JsonProperty("password")
    @Nonnull
    public String getPassword() {
        return password;
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

    private RegisterUserRequest(@NotNull UUID requestId, @NotBlank String username,
            @NotBlank String password, @Email @NotBlank String email, @Nullable String tracingId) {
        this.requestId = requestId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.tracingId = tracingId;
    }

    @JsonCreator
    public static RegisterUserRequest of(@JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("username") @NotBlank String username,
            @JsonProperty("password") @NotBlank String password,
            @JsonProperty("email") @Email @NotBlank String email,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var request = new RegisterUserRequest(requestId, username, password, email, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return request;
    }

    public RegisterUserRequest withRequestId(UUID requestId) {
        return RegisterUserRequest.of(requestId, username, password, email, tracingId);
    }

    public RegisterUserRequest withUsername(String username) {
        return RegisterUserRequest.of(requestId, username, password, email, tracingId);
    }

    public RegisterUserRequest withPassword(String password) {
        return RegisterUserRequest.of(requestId, username, password, email, tracingId);
    }

    public RegisterUserRequest withEmail(String email) {
        return RegisterUserRequest.of(requestId, username, password, email, tracingId);
    }

    public RegisterUserRequest withTracingId(String tracingId) {
        return RegisterUserRequest.of(requestId, username, password, email, tracingId);
    }
}
