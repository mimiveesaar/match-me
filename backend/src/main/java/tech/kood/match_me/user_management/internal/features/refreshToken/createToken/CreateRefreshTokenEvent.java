package tech.kood.match_me.user_management.internal.features.refreshToken.createToken;

public record CreateRefreshTokenEvent(
        CreateRefreshTokenRequest request,
        CreateRefreshTokenResults result) {
}
