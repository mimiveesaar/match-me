package tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

/**
 * Represents a request to validate an access token.
 * <p>
 * This class is immutable and uses Bean Validation annotations to ensure that the provided fields
 * meet the required constraints.
 * </p>
 *
 * <ul>
 * <li>{@code requestId}: A unique identifier for the request. Must not be null.</li>
 * <li>{@code jwtToken}: The JWT token to validate. Must not be null or empty.</li>
 * <li>{@code tracingId}: An optional tracing identifier for request tracking.</li>
 * </ul>
 *
 * <p>
 * Use the static {@link #of(UUID, String, String)} factory method to create instances, which
 * performs validation and throws a {@link jakarta.validation.ConstraintViolationException} if any
 * constraints are violated.
 * </p>
 */
public final class ValidateAccessTokenRequest
        implements tech.kood.match_me.user_management.internal.common.cqrs.Query {

    @NotNull
    private final UUID requestId;

    @NotNull
    private final String jwtToken;

    @Nullable
    private final String tracingId;

    @JsonProperty("requestId")
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty("jwtToken")
    public String getJwtToken() {
        return jwtToken;
    }

    @JsonProperty("tracingId")
    public String getTracingId() {
        return tracingId;
    }

    private ValidateAccessTokenRequest(@NotNull UUID requestId, @NotNull String jwtToken,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.jwtToken = jwtToken;
        this.tracingId = tracingId;
    }

    public static ValidateAccessTokenRequest of(@JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("jwtToken") @NotNull String jwtToken,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var request = new ValidateAccessTokenRequest(requestId, jwtToken, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    "Invalid ValidateAccessTokenRequest: " + violations, violations);
        }
        return request;
    }

    public ValidateAccessTokenRequest withJwtToken(ValidateAccessTokenRequest request,
            String jwtToken) {
        return ValidateAccessTokenRequest.of(request.getRequestId(), jwtToken,
                request.getTracingId());
    }

    public ValidateAccessTokenRequest withTracingId(ValidateAccessTokenRequest request,
            String tracingId) {
        return ValidateAccessTokenRequest.of(request.getRequestId(), request.getJwtToken(),
                tracingId);
    }
}
