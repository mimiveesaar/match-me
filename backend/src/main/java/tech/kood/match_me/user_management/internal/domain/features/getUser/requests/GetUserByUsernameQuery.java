package tech.kood.match_me.user_management.internal.domain.features.getUser.requests;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;


/**
 * Represents a query to retrieve a user by their username.
 * <p>
 * This class is immutable and uses Bean Validation annotations to ensure that the provided fields
 * meet the required constraints.
 * </p>
 *
 * <ul>
 * <li>{@code requestId}: A unique identifier for the request. Must not be null.</li>
 * <li>{@code username}: The username of the user to retrieve. Must be a valid username.</li>
 * <li>{@code tracingId}: An optional tracing identifier for request tracking.</li>
 * </ul>
 *
 * <p>
 * Use the static {@link #of(UUID, String, String)} factory method to create instances, which
 * performs validation and throws a {@link jakarta.validation.ConstraintViolationException} if any
 * constraints are violated.
 * </p>
 */
public final class GetUserByUsernameQuery
        implements tech.kood.match_me.user_management.internal.common.cqrs.Query {

    @NotNull
    private final UUID requestId;

    @NotNull
    private final String username;

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

    @JsonProperty("tracingId")
    @Nullable
    public String getTracingId() {
        return tracingId;
    }

    private GetUserByUsernameQuery(@NotNull UUID requestId, @NotNull String username,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.username = username;
        this.tracingId = tracingId;
    }

    public static GetUserByUsernameQuery of(@JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("username") @NotNull String username,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var query = new GetUserByUsernameQuery(requestId, username, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(query);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid GetUserByUsernameQuery: " + violations,
                    violations);
        }
        return query;
    }

    public GetUserByUsernameQuery withUsername(GetUserByUsernameQuery query, String username) {
        return GetUserByUsernameQuery.of(query.requestId, username, query.tracingId);
    }

    public GetUserByUsernameQuery withTracingId(GetUserByUsernameQuery query, String tracingId) {
        return GetUserByUsernameQuery.of(query.requestId, query.username, tracingId);
    }
}
