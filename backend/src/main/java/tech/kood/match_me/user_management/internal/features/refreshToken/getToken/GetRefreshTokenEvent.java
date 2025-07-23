package tech.kood.match_me.user_management.internal.features.refreshToken.getToken;

public record GetRefreshTokenEvent(
        GetRefreshTokenRequest request,
        GetRefreshTokenResults results) {
    // This record encapsulates the event of getting a refresh token, containing the
    // request and the results.

}
