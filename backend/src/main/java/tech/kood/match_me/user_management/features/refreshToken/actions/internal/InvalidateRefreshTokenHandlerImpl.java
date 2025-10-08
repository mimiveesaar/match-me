package tech.kood.match_me.user_management.features.refreshToken.actions.internal;

import jakarta.validation.Validator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.actions.InvalidateRefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.internal.mapper.RefreshTokenMapper;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;

@Service
public class InvalidateRefreshTokenHandlerImpl implements InvalidateRefreshToken.Handler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Validator validator;
    private final RefreshTokenMapper refreshTokenMapper;
    private final ApplicationEventPublisher events;

    public InvalidateRefreshTokenHandlerImpl(RefreshTokenRepository refreshTokenRepository, Validator validator,
                                                    RefreshTokenMapper refreshTokenMapper, ApplicationEventPublisher events) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.validator = validator;
        this.refreshTokenMapper = refreshTokenMapper;
        this.events = events;
    }

    @Override
    public InvalidateRefreshToken.Result handle(InvalidateRefreshToken.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new InvalidateRefreshToken.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var deletedToken = refreshTokenRepository.deleteToken(request.secret().toString());

            if (deletedToken.isEmpty()) {
                return new InvalidateRefreshToken.Result.TokenNotFound();
            }

            var result = new InvalidateRefreshToken.Result.Success();

            var deletedTokenDTO = refreshTokenMapper.toDTO(deletedToken.get());
            events.publishEvent(new InvalidateRefreshToken.RefreshTokenInvalidated(deletedTokenDTO));
            return result;
        } catch (Exception e) {
            return new InvalidateRefreshToken.Result.SystemError(e.getMessage());
        }
    }
}