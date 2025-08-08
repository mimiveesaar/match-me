package tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Command;
import tech.kood.match_me.user_management.internal.common.cqrs.Query;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

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

    @JsonProperty("requestId")
    @NotNull
    public final UUID requestId;

    @JsonProperty("token")
    @NotBlank
    public final String token;

    @JsonProperty("tracingId")
    @Nullable
    public final String tracingId;

    private GetRefreshTokenRequest(@NotNull UUID requestId, @NotBlank String token,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.token = token;
        this.tracingId = tracingId;
    }

    public static GetRefreshTokenRequest of(@JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("token") @NotBlank String token,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var request = new GetRefreshTokenRequest(requestId, token, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid GetRefreshTokenRequest: " + violations,
                    violations);
        }
        return request;
    }

    public GetRefreshTokenRequest withRequestId(UUID newRequestId) {
        return GetRefreshTokenRequest.of(newRequestId, token, tracingId);
    }

    public GetRefreshTokenRequest withToken(String newToken) {
        return GetRefreshTokenRequest.of(requestId, newToken, tracingId);
    }

    public GetRefreshTokenRequest withTracingId(String newTracingId) {
        return GetRefreshTokenRequest.of(requestId, token, newTracingId);
    }
}
