package tech.kood.match_me.user_management.internal.domain.models;

import java.time.Instant;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

/**
 * Represents a refresh token in the system.
 * <ul>
 * <li>{@code id} - Unique identifier for the refresh token (must not be null).</li>
 * <li>{@code userId} - The user ID associated with the token (must not be null).</li>
 * <li>{@code token} - The token string (must not be null or blank).</li>
 * <li>{@code createdAt} - Timestamp when the token was created (must not be null).</li>
 * <li>{@code expiresAt} - Timestamp when the token expires (must not be null).</li>
 * </ul>
 * <p>
 * Use the static factory method {@link #of(UUID, UUID, String, Instant, Instant)} to create
 * instances, which performs validation. If validation fails, a {@link ConstraintViolationException}
 * is thrown.
 * </p>
 * <p>
 * Provides "with" methods to create modified copies of the refresh token with updated fields.
 * </p>
 */
public final class RefreshToken {


    @NotNull
    private final UUID id;

    @NotNull
    private final UUID userId;

    @NotBlank
    private final String token;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant expiresAt;

    @JsonProperty("id")
    @Nonnull
    public UUID getId() {
        return id;
    }

    @JsonProperty("userId")
    @Nonnull
    public UUID getUserId() {
        return userId;
    }

    @JsonProperty("token")
    @Nonnull
    public String getToken() {
        return token;
    }

    @JsonProperty("createdAt")
    @Nonnull
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("expiresAt")
    @Nonnull
    public Instant getExpiresAt() {
        return expiresAt;
    }

    private RefreshToken(@JsonProperty("id") @NotNull UUID id,
            @JsonProperty("userId") @NotNull UUID userId,
            @JsonProperty("token") @NotBlank String token,
            @JsonProperty("createdAt") @NotNull Instant createdAt,
            @JsonProperty("expiresAt") @NotNull Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    @JsonCreator
    public static RefreshToken of(@JsonProperty("id") @NotNull UUID id,
            @JsonProperty("userId") @NotNull UUID userId,
            @JsonProperty("token") @NotBlank String token,
            @JsonProperty("createdAt") @NotNull Instant createdAt,
            @JsonProperty("expiresAt") @NotNull Instant expiresAt) {
        var refreshToken = new RefreshToken(id, userId, token, createdAt, expiresAt);
        var violations = DomainObjectInputValidator.instance.validate(refreshToken);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid RefreshToken: " + violations,
                    violations);
        }
        return refreshToken;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof RefreshToken))
            return false;
        RefreshToken other = (RefreshToken) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "RefreshToken{" + "id=" + id + ", userId=" + userId + ", token='" + token + '\''
                + ", createdAt=" + createdAt + ", expiresAt=" + expiresAt + '}';
    }

    public RefreshToken withId(UUID newId) {
        return RefreshToken.of(newId, userId, token, createdAt, expiresAt);
    }

    public RefreshToken withUserId(UUID newUserId) {
        return RefreshToken.of(id, newUserId, token, createdAt, expiresAt);
    }

    public RefreshToken withToken(String newToken) {
        return RefreshToken.of(id, userId, newToken, createdAt, expiresAt);
    }

    public RefreshToken withCreatedAt(Instant newCreatedAt) {
        return RefreshToken.of(id, userId, token, newCreatedAt, expiresAt);
    }

    public RefreshToken withExpiresAt(Instant newExpiresAt) {
        return RefreshToken.of(id, userId, token, createdAt, newExpiresAt);
    }
}
