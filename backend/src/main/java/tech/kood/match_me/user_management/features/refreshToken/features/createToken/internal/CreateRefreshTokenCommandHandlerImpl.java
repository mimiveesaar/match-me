package tech.kood.match_me.user_management.features.refreshToken.features.createToken.internal;

import jakarta.validation.Validator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshToken.RefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.RefreshTokenCreated;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.internal.mapper.RefreshTokenMapper;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;


@Service
public class CreateRefreshTokenCommandHandlerImpl implements CreateRefreshTokenCommandHandler {

    private final Validator validator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenFactory refreshTokenFactory;
    private final ApplicationEventPublisher events;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserIdFactory userIdFactory;

    public CreateRefreshTokenCommandHandlerImpl(Validator validator, RefreshTokenRepository refreshTokenRepository,
                                                ApplicationEventPublisher events, RefreshTokenFactory refreshTokenFactory,
                                                RefreshTokenMapper refreshTokenMapper, UserIdFactory userIdFactory) {
        this.validator = validator;
        this.refreshTokenRepository = refreshTokenRepository;
        this.events = events;
        this.refreshTokenFactory = refreshTokenFactory;
        this.refreshTokenMapper = refreshTokenMapper;
        this.userIdFactory = userIdFactory;
    }

    @Override
    public CreateRefreshTokenResults handle(CreateRefreshTokenRequest request) {

        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new CreateRefreshTokenResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.from(validationResults), request.tracingId());
        }

        try {
            var userId = userIdFactory.create(request.requestId());
            RefreshToken refreshToken = refreshTokenFactory.makeNew(userId);

            var refreshTokenEntity = refreshTokenMapper.toEntity(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);

            var result = new CreateRefreshTokenResults.Success(request.requestId(), refreshTokenMapper.toDTO(refreshToken), request.tracingId());
            events.publishEvent(new RefreshTokenCreated(result.refreshToken()));
            return result;

        } catch (Exception e) {
            return new CreateRefreshTokenResults.SystemError(request.requestId(), e.getMessage(), request.tracingId());
        }
    }
}
