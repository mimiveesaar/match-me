package tech.kood.match_me.user_management.features.refreshToken.features.getToken;

public record GetRefreshTokenEvent(
        GetRefreshTokenRequest request,
        GetRefreshTokenResults result) {
}
