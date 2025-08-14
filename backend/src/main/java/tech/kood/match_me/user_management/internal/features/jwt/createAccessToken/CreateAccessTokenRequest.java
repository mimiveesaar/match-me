package tech.kood.match_me.user_management.internal.features.jwt.createAccessToken;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

/**
 * Represents a request to retrieve an access token using a refresh token.
 * <p>
 * This class is immutable and uses Bean Validation annotations to ensure that the provided fields
 * meet the required constraints.
 * </p>
 *
 * <ul>
 * <li>{@code requestId}: A unique identifier for the request. Must not be null.</li>
 * <li>{@code refreshToken}: The refresh token used to generate a new access token. Must not be null
 * or empty.</li>
 * <li>{@code tracingId}: An optional tracing identifier for request tracking.</li>
 * </ul>
 *
 * <p>
 * Use the static {@link #of(UUID, String, String)} factory method to create instances, which
 * performs validation and throws a {@link jakarta.validation.ConstraintViolationException} if any
 * constraints are violated.
 * </p>
 */
public final class CreateAccessTokenRequest
        implements tech.kood.match_me.user_management.internal.common.cqrs.Command {

    @NotNull
    private final UUID requestId;

    @NotEmpty
    private final String refreshToken;

    @Nullable
    private final String tracingId;

    @JsonProperty("requestId")
    @NotNull
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty("refreshToken")
    @NotEmpty
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("tracingId")
    @Nullable
    public String getTracingId() {
        return tracingId;
    }

    private CreateAccessTokenRequest(@NotNull UUID requestId, @NotNull String refreshToken,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.refreshToken = refreshToken;
        this.tracingId = tracingId;
    }

    @JsonCreator
    public static CreateAccessTokenRequest of(@JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("refreshToken") @NotNull String refreshToken,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var request = new CreateAccessTokenRequest(requestId, refreshToken, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid GetAccessTokenRequest: " + violations,
                    violations);
        }
        return request;
    }

    public CreateAccessTokenRequest withRefreshToken(CreateAccessTokenRequest request,
            String refreshToken) {
        return CreateAccessTokenRequest.of(request.getRequestId(), refreshToken,
                request.getTracingId());
    }

    public CreateAccessTokenRequest withTracingId(CreateAccessTokenRequest request,
            String tracingId) {
        return CreateAccessTokenRequest.of(request.getRequestId(), request.getRefreshToken(),
                tracingId);
    }
}
