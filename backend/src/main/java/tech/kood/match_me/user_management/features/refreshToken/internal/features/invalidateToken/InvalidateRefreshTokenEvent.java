package tech.kood.match_me.user_management.features.refreshToken.internal.features.invalidateToken;

public record InvalidateRefreshTokenEvent(
        InvalidateRefreshTokenRequest request,
        InvalidateRefreshTokenResults results) {
}
