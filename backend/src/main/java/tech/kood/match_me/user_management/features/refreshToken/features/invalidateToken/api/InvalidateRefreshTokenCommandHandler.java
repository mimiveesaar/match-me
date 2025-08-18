package tech.kood.match_me.user_management.features.refreshToken.features.invalidateToken.api;

public interface InvalidateRefreshTokenCommandHandler {
    InvalidateRefreshTokenResults handle(InvalidateRefreshTokenRequest request);
}
