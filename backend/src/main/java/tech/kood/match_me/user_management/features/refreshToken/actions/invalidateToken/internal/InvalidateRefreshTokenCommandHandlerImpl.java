package tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.internal;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.RefreshTokenInvalidatedEvent;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.InvalidateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.InvalidateRefreshTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.internal.mapper.RefreshTokenMapper;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;

@Service
public class InvalidateRefreshTokenCommandHandlerImpl implements InvalidateRefreshTokenCommandHandler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Validator validator;
    private final RefreshTokenMapper refreshTokenMapper;
    private final ApplicationEventPublisher events;

    public InvalidateRefreshTokenCommandHandlerImpl(RefreshTokenRepository refreshTokenRepository, Validator validator,
                                                    RefreshTokenMapper refreshTokenMapper, ApplicationEventPublisher events) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.validator = validator;
        this.refreshTokenMapper = refreshTokenMapper;
        this.events = events;
    }

    @Override
    @Transactional
    public InvalidateRefreshTokenResults handle(InvalidateRefreshTokenRequest request) {

        var invalidationResults = validator.validate(request);

        if (!invalidationResults.isEmpty()) {
            return new InvalidateRefreshTokenResults.InvalidRequest(InvalidInputErrorDTO.fromValidation(invalidationResults));
        }

        try {
            var deletedToken = refreshTokenRepository.deleteToken(request.secret().toString());

            if (deletedToken.isEmpty()) {
                return new InvalidateRefreshTokenResults.TokenNotFound();
            }

            var result = new InvalidateRefreshTokenResults.Success();

            var deletedTokenDTO = refreshTokenMapper.toDTO(deletedToken.get());
            events.publishEvent(new RefreshTokenInvalidatedEvent(deletedTokenDTO));
            return result;
        } catch (Exception e) {
            return new InvalidateRefreshTokenResults.SystemError(
                    e.getMessage()
            );
        }
    }
}