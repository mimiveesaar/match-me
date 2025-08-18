package tech.kood.match_me.user_management.features.refreshToken.internal.features.getToken;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Query;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

import java.util.UUID;

/**
 * Represents a request to get a refresh token, containing identifiers for the request, the token,
 * and an optional tracing ID for tracking purposes.
 *
 * <ul>
 * <li>{@code requestId} - Unique identifier for the refresh token request (must not be null).</li>
 * <li>{@code token} - The refresh token to retrieve (must not be blank).</li>
 * <li>{@code tracingId} - Optional tracing identifier for request tracking (nullable).</li>
 * </ul>
 *
 * <p>
 * Use the static factory method {@link #of(String, String, String)} to create instances, which
 * performs validation. If validation fails, a {@link ConstraintViolationException} is thrown.
 * </p>
 *
 * <p>
 * Provides "with" methods to create modified copies of the request with updated fields.
 * </p>
 */
public final class GetRefreshTokenRequest implements Query {

    @NotNull
    private final UUID requestId;

    @NotBlank
    private final String token;

    @Nullable
    private final String tracingId;

    @JsonProperty("requestId")
    @Nonnull
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty("token")
    @Nonnull
    public String getToken() {
        return token;
    }

    @JsonProperty("tracingId")
    @Nullable
    public String getTracingId() {
        return tracingId;
    }

    private GetRefreshTokenRequest(@NotNull UUID requestId, @NotBlank String token,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.token = token;
        this.tracingId = tracingId;
    }

    @JsonCreator
    public static GetRefreshTokenRequest of(@JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("token") @NotBlank String token,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var request = new GetRefreshTokenRequest(requestId, token, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return request;
    }

    public GetRefreshTokenRequest withRequestId(UUID requestId) {
        return GetRefreshTokenRequest.of(requestId, token, tracingId);
    }

    public GetRefreshTokenRequest withToken(String token) {
        return GetRefreshTokenRequest.of(requestId, token, tracingId);
    }

    public GetRefreshTokenRequest withTracingId(String tracingId) {
        return GetRefreshTokenRequest.of(requestId, token, tracingId);
    }
}
