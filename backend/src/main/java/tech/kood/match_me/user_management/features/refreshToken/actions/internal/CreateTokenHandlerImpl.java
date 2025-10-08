package tech.kood.match_me.user_management.features.refreshToken.actions.internal;

import jakarta.validation.Validator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.actions.CreateRefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshToken.RefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.features.refreshToken.internal.mapper.RefreshTokenMapper;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.features.user.actions.GetUserById;


@Service
public class CreateTokenHandlerImpl implements CreateRefreshToken.Handler {

    private final Validator validator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenFactory refreshTokenFactory;
    private final ApplicationEventPublisher events;
    private final GetUserById.Handler getUserByIdQueryHandler;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserIdFactory userIdFactory;

    public CreateTokenHandlerImpl(Validator validator, RefreshTokenRepository refreshTokenRepository,
                                  ApplicationEventPublisher events,
                                  RefreshTokenFactory refreshTokenFactory,
                                  GetUserById.Handler getUserByIdQueryHandler1,
                                  RefreshTokenMapper refreshTokenMapper,
                                  UserIdFactory userIdFactory) {
        this.validator = validator;
        this.refreshTokenRepository = refreshTokenRepository;
        this.events = events;
        this.refreshTokenFactory = refreshTokenFactory;
        this.getUserByIdQueryHandler = getUserByIdQueryHandler1;
        this.refreshTokenMapper = refreshTokenMapper;
        this.userIdFactory = userIdFactory;
    }

    @Override
    @Transactional
    public CreateRefreshToken.Result handle(CreateRefreshToken.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new CreateRefreshToken.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var userId = userIdFactory.from(request.userId());
            var userQueryResult = getUserByIdQueryHandler.handle(new GetUserById.Request(request.userId()));

            if (userQueryResult instanceof GetUserById.Result.UserNotFound) {
                return new CreateRefreshToken.Result.UserNotFound(request.userId());
            }

            RefreshToken refreshToken = refreshTokenFactory.createNew(userId);

            var refreshTokenEntity = refreshTokenMapper.toEntity(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);

            var result = new CreateRefreshToken.Result.Success(refreshTokenMapper.toDTO(refreshToken));
            events.publishEvent(new CreateRefreshToken.RefreshTokenCreated(result.refreshToken()));

            return result;
        } catch (Exception e) {
            return new CreateRefreshToken.Result.SystemError(e.getMessage());
        }
    }
}