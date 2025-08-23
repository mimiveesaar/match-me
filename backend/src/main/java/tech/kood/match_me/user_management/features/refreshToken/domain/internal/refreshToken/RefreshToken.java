package tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshToken;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.AggregateRoot;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshTokenId.RefreshTokenId;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshTokenSecret.RefreshTokenSecret;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserId;

import java.time.Instant;
import java.util.Objects;

@DomainLayer
public final class RefreshToken implements AggregateRoot<RefreshToken, RefreshTokenId> {

    @NotNull
    @Valid
    private final RefreshTokenId id;

    @NotNull
    @Valid
    private final UserId userId;

    @NotNull
    private final RefreshTokenSecret refreshTokenSecret;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant expiresAt;

    @Override
    public RefreshTokenId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public RefreshTokenSecret getSecret() {
        return refreshTokenSecret;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    RefreshToken(RefreshTokenId id, UserId userId, RefreshTokenSecret refreshTokenSecret, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.refreshTokenSecret = refreshTokenSecret;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id.toString() +
                ", userId=" + userId.toString() +
                ", secret='" + refreshTokenSecret.toString() + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
