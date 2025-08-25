package tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api;

public interface GetRefreshTokenQueryHandler {
    GetRefreshTokenResults handle(GetRefreshTokenRequest request);
}
