package tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import tech.kood.match_me.user_management.internal.common.cqrs.Command;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.domain.models.User;

/**
 * Represents a request to refresh a token, containing identifiers for the request, the user, and an
 * optional tracing ID for tracking purposes.
 *
 * <ul>
 * <li>{@code requestId} - Unique identifier for the refresh token request (must not be null).</li>
 * <li>{@code user} - User for whom the refresh token is being created (must not be null).</li>
 * <li>{@code tracingId} - Optional tracing identifier for request tracking (nullable).</li>
 * </ul>
 *
 * <p>
 * Use the static factory method {@link #of(String, User, String)} to create instances, which
 * performs validation. If validation fails, a {@link ConstraintViolationException} is thrown.
 * </p>
 *
 * <p>
 * Provides "with" methods to create modified copies of the request with updated fields.
 * </p>
 */
public final class CreateRefreshTokenRequest implements Command {

    @UUID
    private final String requestId;

    @NotNull
    private final User user;

    @Nullable
    private final String tracingId;


    @JsonProperty("requestId")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    @JsonProperty("tracingId")
    public String getTracingId() {
        return tracingId;
    }

    private CreateRefreshTokenRequest(@UUID String requestId, @NotNull User user,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.user = user;
        this.tracingId = tracingId;
    }


    public static CreateRefreshTokenRequest of(@JsonProperty("requestId") @UUID String requestId,
            @JsonProperty("user") @NotNull User user,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var request = new CreateRefreshTokenRequest(requestId, user, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    "Invalid CreateRefreshTokenRequest: " + violations, violations);
        }
        return request;
    }

    public CreateRefreshTokenRequest withRequestId(String newRequestId) {
        return CreateRefreshTokenRequest.of(newRequestId, user, tracingId);
    }

    public CreateRefreshTokenRequest withUser(User newUser) {
        return CreateRefreshTokenRequest.of(requestId, newUser, tracingId);
    }

    public CreateRefreshTokenRequest withTracingId(String newTracingId) {
        return CreateRefreshTokenRequest.of(requestId, user, newTracingId);
    }
}
