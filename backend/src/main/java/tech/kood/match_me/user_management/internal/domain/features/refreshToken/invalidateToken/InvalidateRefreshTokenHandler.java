package tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;

@Service
public class InvalidateRefreshTokenHandler {

    private final RefreshTokenRepository refreshTokenRepository;

    private final ApplicationEventPublisher events;

    public InvalidateRefreshTokenHandler(RefreshTokenRepository refreshTokenRepository,
            ApplicationEventPublisher events) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.events = events;
    }

    public InvalidateRefreshTokenResults handle(InvalidateRefreshTokenRequest request) {
        try {

            if (request.token() == null || request.token().isBlank()) {
                var result = new InvalidateRefreshTokenResults.InvalidRequest(
                        "Token must not be null or blank", request.tracingId());
                events.publishEvent(new InvalidateRefreshTokenEvent(request, result));
                return result;
            }

            var tokenExists = refreshTokenRepository.deleteToken(request.token());

            if (!tokenExists) {
                var tokenNotFoundResult = new InvalidateRefreshTokenResults.TokenNotFound(
                        request.token(), request.tracingId());
                events.publishEvent(new InvalidateRefreshTokenEvent(request, tokenNotFoundResult));
                return tokenNotFoundResult;
            }

            var result = new InvalidateRefreshTokenResults.Success(request.tracingId());
            events.publishEvent(new InvalidateRefreshTokenEvent(request, result));
            return result;
        } catch (Exception e) {
            var result = new InvalidateRefreshTokenResults.SystemError(
                    "An unexpected error occurred: " + e.getMessage(), request.tracingId());
            events.publishEvent(new InvalidateRefreshTokenEvent(request, result));
            return result;
        }
    }
}
