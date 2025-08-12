package tech.kood.match_me.user_management.internal.database.entities;

import java.time.Instant;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

public class RefreshTokenEntity {

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
    public UUID getId() {
        return id;
    }

    @JsonProperty("userId")
    public UUID getUserId() {
        return userId;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("createdAt")
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("expiresAt")
    public Instant getExpiresAt() {
        return expiresAt;
    }

    private RefreshTokenEntity(@JsonProperty("id") UUID id, @JsonProperty("userId") UUID userId,
            @JsonProperty("token") String token, @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("expiresAt") Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    @JsonCreator
    public static RefreshTokenEntity of(@JsonProperty("id") UUID id,
            @JsonProperty("userId") UUID userId, @JsonProperty("token") String token,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("expiresAt") Instant expiresAt) {
        var entity = new RefreshTokenEntity(id, userId, token, createdAt, expiresAt);
        var violations = DomainObjectInputValidator.instance.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return entity;
    }

    public RefreshTokenEntity withId(UUID id) {
        return RefreshTokenEntity.of(id, this.userId, this.token, this.createdAt, this.expiresAt);
    }

    public RefreshTokenEntity withUserId(UUID userId) {
        return RefreshTokenEntity.of(this.id, userId, this.token, this.createdAt, this.expiresAt);
    }

    public RefreshTokenEntity withToken(String token) {
        return RefreshTokenEntity.of(this.id, this.userId, token, this.createdAt, this.expiresAt);
    }

    public RefreshTokenEntity withCreatedAt(Instant createdAt) {
        return RefreshTokenEntity.of(this.id, this.userId, this.token, createdAt, this.expiresAt);
    }

    public RefreshTokenEntity withExpiresAt(Instant expiresAt) {
        return RefreshTokenEntity.of(this.id, this.userId, this.token, this.createdAt, expiresAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RefreshTokenEntity that = (RefreshTokenEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RefreshTokenEntity{" + "id=" + id + ", userId=" + userId + ", createdAt="
                + createdAt + ", expiresAt=" + expiresAt + '}';
    }
}
