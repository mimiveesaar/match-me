package tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.common.cqrs.CommandHandler;
import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.mappers.RefreshTokenMapper;

@Service
public final class InvalidateRefreshTokenHandler
        implements CommandHandler<InvalidateRefreshTokenRequest, InvalidateRefreshTokenResults> {

    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenMapper refreshTokenMapper;

    private final ApplicationEventPublisher events;

    public InvalidateRefreshTokenHandler(RefreshTokenRepository refreshTokenRepository,
            RefreshTokenMapper refreshTokenMapper, ApplicationEventPublisher events) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
        this.events = events;
    }

    /**
     * Handles the invalidation of a refresh token based on the provided request.
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Attempts to delete a refresh token entity using the provided token.</li>
     * <li>If the token doesn't exist, returns a {@code TokenNotFound} result.</li>
     * <li>If the token exists and is successfully deleted, returns a {@code Success} result.</li>
     * <li>In case of any unexpected exceptions, returns a {@code SystemError} result.</li>
     * <li>Publishes a {@code InvalidateRefreshTokenEvent} with the request and result at each
     * step.</li>
     * </ul>
     *
     * @param request the {@link InvalidateRefreshTokenRequest} containing the token and tracing
     *        information
     * @return a {@link InvalidateRefreshTokenResults} representing the outcome of the operation
     */
    public InvalidateRefreshTokenResults handle(InvalidateRefreshTokenRequest request) {
        try {
            var tokenExists = refreshTokenRepository.deleteToken(request.getToken());

            if (!tokenExists) {
                var result = InvalidateRefreshTokenResults.TokenNotFound.of(request.getToken(),
                        request.getRequestId(), request.getTracingId());
                events.publishEvent(new InvalidateRefreshTokenEvent(request, result));
                return result;
            }

            var result = InvalidateRefreshTokenResults.Success.of(request.getRequestId(),
                    request.getTracingId());
            events.publishEvent(new InvalidateRefreshTokenEvent(request, result));
            return result;
        } catch (Exception e) {
            var result = InvalidateRefreshTokenResults.SystemError.of(
                    "An unexpected error occurred: " + e.getMessage(), request.getRequestId(),
                    request.getTracingId());
            events.publishEvent(new InvalidateRefreshTokenEvent(request, result));
            return result;
        }
    }
}
