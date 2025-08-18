package tech.kood.match_me.user_management.features.refreshToken.features.invalidateToken.api;

public interface InvalidateRefreshTokenHandler {
    InvalidateRefreshTokenResults handle(InvalidateRefreshTokenRequest request);
}
