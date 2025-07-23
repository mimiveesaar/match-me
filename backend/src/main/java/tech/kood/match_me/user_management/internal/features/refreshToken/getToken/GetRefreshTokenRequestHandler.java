package tech.kood.match_me.user_management.internal.features.refreshToken.getToken;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.mappers.RefreshTokenMapper;

@Service
public final class GetRefreshTokenRequestHandler {

    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenMapper refreshTokenMapper;

    private final ApplicationEventPublisher events;

    public GetRefreshTokenRequestHandler(
            RefreshTokenRepository refreshTokenRepository,
            RefreshTokenMapper refreshTokenMapper,
            ApplicationEventPublisher events) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
        this.events = events;
    }

    public GetRefreshTokenResults handle(GetRefreshTokenRequest request) {
        if (request.token() == null || request.token().isBlank()) {
            var result = new GetRefreshTokenResults.InvalidRequest("Token must not be null or empty",
                    request.tracingId());
            events.publishEvent(new GetRefreshTokenEvent(request, result));
            return result;
        }

        var tokenEntity = refreshTokenRepository.findToken(request.token());
        if (tokenEntity.isEmpty()) {
            var result = new GetRefreshTokenResults.InvalidToken("Invalid refresh token", request.tracingId());
            events.publishEvent(new GetRefreshTokenEvent(request, result));
            return result;
        }

        var result = new GetRefreshTokenResults.Success(refreshTokenMapper.toRefreshToken(tokenEntity.get()),
                request.tracingId());
        events.publishEvent(new GetRefreshTokenEvent(request, result));
        return result;
    }
}
