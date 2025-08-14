package tech.kood.match_me.user_management.internal.features.refreshToken.getToken;

public record GetRefreshTokenEvent(
        GetRefreshTokenRequest request,
        GetRefreshTokenResults result) {
}
