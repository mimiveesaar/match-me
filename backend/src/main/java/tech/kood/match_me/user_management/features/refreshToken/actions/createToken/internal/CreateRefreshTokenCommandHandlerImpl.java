package tech.kood.match_me.user_management.features.refreshToken.actions.createToken.internal;

import jakarta.validation.Validator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshToken.RefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.RefreshTokenCreated;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.internal.mapper.RefreshTokenMapper;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByIdQueryHandler;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByIdRequest;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByIdResults;


@Service
public class CreateRefreshTokenCommandHandlerImpl implements CreateRefreshTokenCommandHandler {

    private final Validator validator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenFactory refreshTokenFactory;
    private final ApplicationEventPublisher events;
    private final GetUserByIdQueryHandler getUserByIdQueryHandler;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserIdFactory userIdFactory;

    public CreateRefreshTokenCommandHandlerImpl(Validator validator, RefreshTokenRepository refreshTokenRepository,
                                                ApplicationEventPublisher events, RefreshTokenFactory refreshTokenFactory, GetUserByIdQueryHandler getUserByIdQueryHandler,
                                                RefreshTokenMapper refreshTokenMapper, UserIdFactory userIdFactory) {
        this.validator = validator;
        this.refreshTokenRepository = refreshTokenRepository;
        this.events = events;
        this.refreshTokenFactory = refreshTokenFactory;
        this.getUserByIdQueryHandler = getUserByIdQueryHandler;
        this.refreshTokenMapper = refreshTokenMapper;
        this.userIdFactory = userIdFactory;
    }

    @Override
    @Transactional
    public CreateRefreshTokenResults handle(CreateRefreshTokenRequest request) {

        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new CreateRefreshTokenResults.InvalidRequest(InvalidInputErrorDTO.from(validationResults));
        }

        try {
            var userId = userIdFactory.from(request.userId());

            var userQueryResult = getUserByIdQueryHandler.handle(new GetUserByIdRequest(request.userId()));

            if (userQueryResult instanceof GetUserByIdResults.UserNotFound) {
                return new CreateRefreshTokenResults.UserNotFound(request.userId());
            }

            RefreshToken refreshToken = refreshTokenFactory.createNew(userId);

            var refreshTokenEntity = refreshTokenMapper.toEntity(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);

            var result = new CreateRefreshTokenResults.Success(refreshTokenMapper.toDTO(refreshToken));
            events.publishEvent(new RefreshTokenCreated(result.refreshToken()));
            return result;

        } catch (Exception e) {
            return new CreateRefreshTokenResults.SystemError(e.getMessage());
        }
    }
}
