package tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken;

public record InvalidateRefreshTokenEvent(
        InvalidateRefreshTokenRequest request,
        InvalidateRefreshTokenResults results) {
}
