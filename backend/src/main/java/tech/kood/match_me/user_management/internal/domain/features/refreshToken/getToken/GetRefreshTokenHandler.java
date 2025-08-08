package tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken;

import java.time.Instant;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.common.cqrs.QueryHandler;
import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.mappers.RefreshTokenMapper;

@Service
public final class GetRefreshTokenHandler
        implements QueryHandler<GetRefreshTokenRequest, GetRefreshTokenResults> {

    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenMapper refreshTokenMapper;

    private final ApplicationEventPublisher events;

    public GetRefreshTokenHandler(RefreshTokenRepository refreshTokenRepository,
            RefreshTokenMapper refreshTokenMapper, ApplicationEventPublisher events) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
        this.events = events;
    }

    /**
     * Handles the retrieval and validation of a refresh token based on the provided request.
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Attempts to find a valid refresh token entity using the provided token and the current
     * time.</li>
     * <li>If the token is invalid or not found, returns an {@code InvalidToken} result.</li>
     * <li>If the token is valid, maps it to a domain object and returns a {@code Success}
     * result.</li>
     * <li>In case of any unexpected exceptions, returns a {@code SystemError} result.</li>
     * <li>Publishes a {@code GetRefreshTokenEvent} with the request and result at each step.</li>
     * </ul>
     *
     * @param request the {@link GetRefreshTokenRequest} containing the token and tracing
     *        information
     * @return a {@link GetRefreshTokenResults} representing the outcome of the operation
     */
    public GetRefreshTokenResults handle(GetRefreshTokenRequest request) {
        try {
            var tokenEntity =
                    refreshTokenRepository.findValidToken(request.getToken(), Instant.now());
            if (tokenEntity.isEmpty()) {
                var result = GetRefreshTokenResults.InvalidToken.of(request.getToken(),
                        request.getRequestId(), request.getTracingId());
                events.publishEvent(new GetRefreshTokenEvent(request, result));
                return result;
            }

            var result = GetRefreshTokenResults.Success.of(
                    refreshTokenMapper.toRefreshToken(tokenEntity.get()), request.getRequestId(),
                    request.getTracingId());
            events.publishEvent(new GetRefreshTokenEvent(request, result));
            return result;
        } catch (Exception e) {
            var result = GetRefreshTokenResults.SystemError.of(
                    "An unexpected error occurred: " + e.getMessage(), request.getRequestId(),
                    request.getTracingId());
            events.publishEvent(new GetRefreshTokenEvent(request, result));
            return result;
        }
    }
}
