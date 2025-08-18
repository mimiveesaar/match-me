package tech.kood.match_me.user_management.features.refreshToken.internal.features.invalidateToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Command;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

/**
 * Represents a request to invalidate a refresh token, containing identifiers for the request, the
 * token, and an optional tracing ID for tracking purposes.
 *
 * <ul>
 * <li>{@code requestId} - Unique identifier for the refresh token request (must not be null).</li>
 * <li>{@code token} - The refresh token to invalidate (must not be blank).</li>
 * <li>{@code tracingId} - Optional tracing identifier for request tracking (nullable).</li>
 * </ul>
 *
 * <p>
 * Use the static factory method {@link #of(UUID, String, String)} to create instances, which
 * performs validation. If validation fails, a {@link ConstraintViolationException} is thrown.
 * </p>
 *
 * <p>
 * Provides "with" methods to create modified copies of the request with updated fields.
 * </p>
 */
public final class InvalidateRefreshTokenRequest implements Command {


    @NotNull
    private final UUID requestId;

    @NotBlank
    private final String refreshToken;

    @Nullable
    private final String tracingId;

    @JsonProperty("requestId")
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty("refreshToken")
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("tracingId")
    public String getTracingId() {
        return tracingId;
    }

    private InvalidateRefreshTokenRequest(@NotNull UUID requestId, @NotBlank String token,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.refreshToken = token;
        this.tracingId = tracingId;
    }

    @JsonCreator
    public static InvalidateRefreshTokenRequest of(
            @JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("refreshToken") @NotBlank String refreshToken,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var request = new InvalidateRefreshTokenRequest(requestId, refreshToken, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return request;
    }

    public InvalidateRefreshTokenRequest withRequestId(UUID requestId) {
        return InvalidateRefreshTokenRequest.of(requestId, refreshToken, tracingId);
    }

    public InvalidateRefreshTokenRequest withToken(String token) {
        return InvalidateRefreshTokenRequest.of(requestId, token, tracingId);
    }

    public InvalidateRefreshTokenRequest withTracingId(String tracingId) {
        return InvalidateRefreshTokenRequest.of(requestId, refreshToken, tracingId);
    }
}
