package tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken;

public record GetRefreshTokenEvent(
        GetRefreshTokenRequest request,
        GetRefreshTokenResults result) {
}
