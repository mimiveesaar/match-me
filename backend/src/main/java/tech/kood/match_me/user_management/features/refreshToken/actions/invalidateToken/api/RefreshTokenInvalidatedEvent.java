package tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api;

import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;

public record RefreshTokenInvalidatedEvent(RefreshTokenDTO invalidatedRefreshToken) {}
