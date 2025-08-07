package tech.kood.match_me.user_management.internal.domain.features.refreshToken;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.internal.domain.models.RefreshToken;
import tech.kood.match_me.user_management.internal.utils.RefreshTokenUtils;

@Component
public final class RefreshTokenFactory {

    private final UserManagementConfig userManagementConfig;

    public RefreshTokenFactory(UserManagementConfig userManagementConfig) {
        this.userManagementConfig = userManagementConfig;
    }

    public RefreshToken create(UUID userId) {
        var id = UUID.randomUUID();
        var token = RefreshTokenUtils.generateToken();
        var createdAt = Instant.now();
        var expiresAt = createdAt.plusSeconds(userManagementConfig.getRefreshTokenExpiration()); // 1 hour expiration

        return new RefreshToken(id, userId, token, createdAt, expiresAt);
    }

    public RefreshToken create(UUID userId, Instant expiresAt) {
        var id = UUID.randomUUID();
        var token = RefreshTokenUtils.generateToken();
        var createdAt = Instant.now();

        return new RefreshToken(id, userId, token, createdAt, expiresAt);
    }
}