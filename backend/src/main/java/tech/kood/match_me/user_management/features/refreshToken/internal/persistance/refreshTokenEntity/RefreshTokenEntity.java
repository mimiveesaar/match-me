package tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.InfrastructureLayer;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@InfrastructureLayer
public final class RefreshTokenEntity {
    @NotNull
    private final UUID id;

    @NotNull
    private final UUID userId;

    @NotBlank
    private final String secret;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant expiresAt;

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
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

    public RefreshTokenEntity(UUID id, UUID userId, String secret, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.secret = secret;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RefreshTokenEntity that = (RefreshTokenEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RefreshTokenEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", secret='" + secret + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
