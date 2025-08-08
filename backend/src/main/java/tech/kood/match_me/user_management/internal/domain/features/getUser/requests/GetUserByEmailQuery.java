package tech.kood.match_me.user_management.internal.domain.features.getUser.requests;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;


/**
 * Represents a query to retrieve a user by their email address.
 * <p>
 * This class is immutable and uses Bean Validation annotations to ensure that the provided fields
 * meet the required constraints.
 * </p>
 *
 * <ul>
 * <li>{@code requestId}: A unique identifier for the request. Must not be null.</li>
 * <li>{@code email}: The email address of the user to retrieve. Must be a valid email address.</li>
 * <li>{@code tracingId}: An optional tracing identifier for request tracking.</li>
 * </ul>
 *
 * <p>
 * Use the static {@link #of(UUID, String, String)} factory method to create instances, which
 * performs validation and throws a {@link jakarta.validation.ConstraintViolationException} if any
 * constraints are violated.
 * </p>
 */
public final class GetUserByEmailQuery
        implements tech.kood.match_me.user_management.internal.common.cqrs.Query {

    @NotNull
    private final UUID requestId;

    @Email
    @NotNull
    private final String email;

    @Nullable
    private final String tracingId;

    @JsonProperty("requestId")
    @Nonnull
    public UUID getRequestId() {
        return requestId;
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

    private GetUserByEmailQuery(@NotNull UUID requestId, @Email String email,
            @Nullable String tracingId) {
        this.requestId = requestId;
        this.email = email;
        this.tracingId = tracingId;
    }

    @JsonCreator
    public static GetUserByEmailQuery of(@JsonProperty("requestId") @NotNull UUID requestId,
            @JsonProperty("email") @Email String email,
            @JsonProperty("tracingId") @Nullable String tracingId) {
        var query = new GetUserByEmailQuery(requestId, email, tracingId);
        var violations = DomainObjectInputValidator.instance.validate(query);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid GetUserByEmailRequest: " + violations,
                    violations);
        }
        return query;
    }

    public GetUserByEmailQuery withEmail(GetUserByEmailQuery query, String email) {
        return GetUserByEmailQuery.of(query.requestId, email, query.tracingId);
    }

    public GetUserByEmailQuery withTracingId(GetUserByEmailQuery query, String tracingId) {
        return GetUserByEmailQuery.of(query.requestId, query.email, tracingId);
    }
}
