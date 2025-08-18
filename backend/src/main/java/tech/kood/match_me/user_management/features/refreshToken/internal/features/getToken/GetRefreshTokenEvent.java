package tech.kood.match_me.user_management.features.refreshToken.internal.features.getToken;

public record GetRefreshTokenEvent(
        GetRefreshTokenRequest request,
        GetRefreshTokenResults result) {
}
