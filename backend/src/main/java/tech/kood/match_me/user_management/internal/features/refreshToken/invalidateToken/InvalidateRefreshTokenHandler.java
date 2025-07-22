package tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken;

import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;

@Service
public class InvalidateRefreshTokenHandler {

    private final RefreshTokenRepository refreshTokenRepository;

    public InvalidateRefreshTokenHandler(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public InvalidateRefreshTokenResults handle(InvalidateRefreshTokenRequest request) {

        try {

            if (request.token() == null || request.token().isBlank()) {
                return new InvalidateRefreshTokenResults.InvalidRequest("Token must not be null or blank",
                        request.tracingId());
            }

            var result = refreshTokenRepository.deleteToken(request.token());

            if (!result) {
                return new InvalidateRefreshTokenResults.TokenNotFound(request.token(), request.tracingId());
            }

            return new InvalidateRefreshTokenResults.Success("Token invalidated successfully");
        } catch (Exception e) {
            return new InvalidateRefreshTokenResults.SystemError("An unexpected error occurred: " + e.getMessage(),
                    request.tracingId());
        }
    }
}