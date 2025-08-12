package tech.kood.match_me.user_management.internal.domain.features.login;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.common.cqrs.Command;

/**
 * Represents a login request command containing user credentials and optional tracing information.
 * <p>
 * This class is immutable and uses validation annotations to ensure that all required fields are
 * present and valid.
 * </p>
 *
 * <ul>
 * <li>{@code requestId} - Unique identifier for the login request (must not be null).</li>
 * <li>{@code email} - User's email address (must not be empty).</li>
 * <li>{@code password} - User's password (must not be empty).</li>
 * <li>{@code tracingId} - Optional tracing identifier for request tracking (nullable).</li>
 * </ul>
 *
 * <p>
 * Use the static factory method {@link #of(UUID, String, String, String)} to create instances,
 * which performs validation. If validation fails, a {@link ConstraintViolationException} is thrown.
 * </p>
 *
 * <p>
 * Provides "with" methods to create modified copies of the request with updated fields.
 * </p>
 */
public final class LoginRequest implements Command {

    @NotNull
    private final UUID requestId;

    @NotEmpty
    private final String email;

    @NotEmpty
    private final String password;

    @Nullable
    private final String tracingId;

    @JsonProperty("requestId")
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("tracingId")
    public String getTracingId() {
        return tracingId;
    }

    private LoginRequest(@NotNull UUID requestId, @NotNull String email, @NotNull String password,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.email = email;
        this.password = password;
        this.tracingId = tracingId;
    }

    @JsonCreator
    public static LoginRequest of(@JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("email") @NotNull String email,
            @JsonProperty("password") @NotNull String password,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var request = new LoginRequest(requestId, email, password, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid LoginRequest: " + violations,
                    violations);
        }
        return request;
    }

    public LoginRequest withEmail(LoginRequest request, String email) {
        return LoginRequest.of(request.requestId, email, request.password, request.tracingId);
    }

    public LoginRequest withPassword(LoginRequest request, String password) {
        return LoginRequest.of(request.requestId, request.email, password, request.tracingId);
    }

    public LoginRequest withTracingId(LoginRequest request, String tracingId) {
        return LoginRequest.of(request.requestId, request.email, request.password, tracingId);
    }
}
