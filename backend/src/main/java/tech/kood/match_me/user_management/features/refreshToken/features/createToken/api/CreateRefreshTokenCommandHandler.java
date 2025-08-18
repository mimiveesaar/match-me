package tech.kood.match_me.user_management.features.refreshToken.features.createToken.api;

public interface CreateRefreshTokenCommandHandler {
    CreateRefreshTokenResults handle(CreateRefreshTokenRequest request);
}
