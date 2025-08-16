package tech.kood.match_me.user_management.internal.features.refreshToken.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.AggregateRoot;
import tech.kood.match_me.user_management.internal.features.refreshToken.domain.model.refreshTokenId.RefreshTokenId;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;

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

    @NotBlank
    private final String secret;

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

    public String getSecret() {
        return secret;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    RefreshToken(RefreshTokenId id, UserId userId, String secret, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.secret = secret;
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
                "id=" + id +
                ", userId=" + userId +
                ", secret='" + secret + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
