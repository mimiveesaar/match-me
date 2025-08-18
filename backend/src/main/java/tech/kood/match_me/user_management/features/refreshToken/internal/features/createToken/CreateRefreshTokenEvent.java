package tech.kood.match_me.user_management.features.refreshToken.internal.features.createToken;

public record CreateRefreshTokenEvent(
        CreateRefreshTokenRequest request,
        CreateRefreshTokenResults result) {
}
