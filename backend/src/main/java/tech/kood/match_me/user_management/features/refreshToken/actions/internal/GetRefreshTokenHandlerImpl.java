package tech.kood.match_me.user_management.features.refreshToken.actions.internal;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.actions.GetRefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.internal.mapper.RefreshTokenMapper;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;

import java.time.Instant;

@Service
public class GetRefreshTokenHandlerImpl implements GetRefreshToken.Handler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenMapper refreshTokenMapper;
    private final Validator validator;

    public GetRefreshTokenHandlerImpl(RefreshTokenRepository refreshTokenRepository,
                                      RefreshTokenMapper refreshTokenMapper, Validator validator) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
        this.validator = validator;
    }

    @Override
    @Transactional
    public GetRefreshToken.Result handle(GetRefreshToken.Request request) {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new GetRefreshToken.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var tokenEntity = refreshTokenRepository.findValidToken(request.secret().toString(), Instant.now());
            if (tokenEntity.isEmpty()) {
                return new GetRefreshToken.Result.InvalidSecret();
            }

            var result = refreshTokenMapper.toDTO(tokenEntity.get());
            return new GetRefreshToken.Result.Success(result);
        } catch (Exception e) {
            return new GetRefreshToken.Result.SystemError(e.getMessage());
        }
    }
}