package tech.kood.match_me.user_management.features.refreshToken.features.getToken.api;

public interface GetRefreshTokenHandler {
    GetRefreshTokenResults handle(GetRefreshTokenRequest request);
}
