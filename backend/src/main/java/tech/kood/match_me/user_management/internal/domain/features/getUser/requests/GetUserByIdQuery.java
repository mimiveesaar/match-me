package tech.kood.match_me.user_management.internal.domain.features.getUser.requests;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.cqrs.Query;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;
import tech.kood.match_me.user_management.internal.domain.models.UserId;


/**
 * Represents a query to retrieve a user by their ID.
 * <p>
 * This class is immutable and uses Bean Validation annotations to ensure that the provided fields
 * meet the required constraints.
 * </p>
 *
 * <ul>
 * <li>{@code requestId}: A unique identifier for the request. Must not be null.</li>
 * <li>{@code userId}: The ID of the user to retrieve. Must not be null.</li>
 * <li>{@code tracingId}: An optional tracing identifier for request tracking.</li>
 * </ul>
 *
 * <p>
 * Use the static {@link #of(UUID, UserId, String)} factory method to create instances, which
 * performs validation and throws a {@link jakarta.validation.ConstraintViolationException} if any
 * constraints are violated.
 * </p>
 */

public final class GetUserByIdQuery implements Query {

    @NotNull
    private final UUID requestId;

    @NotNull
    private final UserId userId;

    @Nullable
    private final String tracingId;

    @Nonnull
    @JsonProperty("requestId")
    public UUID getRequestId() {
        return requestId;
    }

    @Nonnull
    @JsonProperty("userId")
    public UserId getUserId() {
        return userId;
    }

    @Nullable
    @JsonProperty("tracingId")
    public String getTracingId() {
        return tracingId;
    }

    private GetUserByIdQuery(@NotNull UUID requestId, @NotNull UserId userId,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.userId = userId;
        this.tracingId = tracingId;
    }

    @JsonCreator
    public static GetUserByIdQuery of(@JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("userId") @NotNull UserId userId,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var query = new GetUserByIdQuery(requestId, userId, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(query);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid GetUserByIdQuery: " + violations,
                    violations);
        }

        return query;
    }

    public GetUserByIdQuery withUserId(GetUserByIdQuery query, UserId newUserId) {
        return GetUserByIdQuery.of(query.requestId, newUserId, query.tracingId);
    }

    public GetUserByIdQuery withTracingId(GetUserByIdQuery query, String newTracingId) {
        return GetUserByIdQuery.of(query.requestId, query.userId, newTracingId);
    }
}
