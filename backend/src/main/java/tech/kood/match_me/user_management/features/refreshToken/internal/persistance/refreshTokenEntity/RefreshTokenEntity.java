package tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity;

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

    @NotNull
    private final UUID sharedSecret;

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

    public UUID getSharedSecret() {
        return sharedSecret;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public RefreshTokenEntity(UUID id, UUID userId, UUID sharedSecret, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.sharedSecret = sharedSecret;
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
                "id=" + id.toString() +
                ", userId=" + userId.toString() +
                ", sharedSecret='" + sharedSecret.toString() + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
