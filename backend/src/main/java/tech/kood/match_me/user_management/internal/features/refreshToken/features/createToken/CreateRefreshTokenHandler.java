package tech.kood.match_me.user_management.internal.features.refreshToken.features.createToken;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.common.cqrs.CommandHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.features.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.internal.mappers.RefreshTokenMapper;


@Service
public class CreateRefreshTokenHandler
        implements CommandHandler<CreateRefreshTokenRequest, CreateRefreshTokenResults> {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenFactory refreshTokenFactory;
    private final ApplicationEventPublisher events;
    private final RefreshTokenMapper refreshTokenMapper;

    public CreateRefreshTokenHandler(RefreshTokenRepository refreshTokenRepository,
            ApplicationEventPublisher events, RefreshTokenFactory refreshTokenFactory,
            RefreshTokenMapper refreshTokenMapper) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.events = events;
        this.refreshTokenFactory = refreshTokenFactory;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    public CreateRefreshTokenResults handle(CreateRefreshTokenRequest request) {

        try {
            // We assume the user exists, as this is a refresh token operation.
            // Create a new refresh token
            RefreshToken refreshToken =
                    this.refreshTokenFactory.create(request.user().getId().getValue());
            var refreshTokenEntity = refreshTokenMapper.toEntity(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);

            var result = CreateRefreshTokenResults.Success.of(refreshToken, request.requestId(),
                    request.tracingId());
            events.publishEvent(new CreateRefreshTokenEvent(request, result));
            return result;
        } catch (Exception e) {
            var result = CreateRefreshTokenResults.SystemError.of("An unexpected error occurred",
                    request.requestId(), request.tracingId());
            events.publishEvent(new CreateRefreshTokenEvent(request, result));
            return result;
        }
    }
}
