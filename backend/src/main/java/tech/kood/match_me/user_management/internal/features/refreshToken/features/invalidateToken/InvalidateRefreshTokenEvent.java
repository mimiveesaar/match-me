package tech.kood.match_me.user_management.internal.features.refreshToken.features.invalidateToken;

public record InvalidateRefreshTokenEvent(
        InvalidateRefreshTokenRequest request,
        InvalidateRefreshTokenResults results) {
}
