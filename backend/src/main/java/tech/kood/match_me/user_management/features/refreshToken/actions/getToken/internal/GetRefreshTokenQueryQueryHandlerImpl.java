package tech.kood.match_me.user_management.features.refreshToken.actions.getToken.internal;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api.GetRefreshTokenQueryHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api.GetRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api.GetRefreshTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.internal.mapper.RefreshTokenMapper;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;

import java.time.Instant;

@Service
public final class GetRefreshTokenQueryQueryHandlerImpl implements GetRefreshTokenQueryHandler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenMapper refreshTokenMapper;
    private final Validator validator;

    public GetRefreshTokenQueryQueryHandlerImpl(RefreshTokenRepository refreshTokenRepository,
                                                RefreshTokenMapper refreshTokenMapper, Validator validator) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
        this.validator = validator;
    }

    @Override
    public GetRefreshTokenResults handle(GetRefreshTokenRequest request) {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new GetRefreshTokenResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.from(validationResults), request.tracingId());
        }

        try {
            var tokenEntity = refreshTokenRepository.findValidToken(request.secret().toString(), Instant.now());
            if (tokenEntity.isEmpty()) {
                return new GetRefreshTokenResults.InvalidSecret(request.requestId(), request.tracingId());
            }

            var result = refreshTokenMapper.toDTO(tokenEntity.get());
            return new GetRefreshTokenResults.Success(request.requestId(), result, request.tracingId());
        } catch (Exception e) {
            return new GetRefreshTokenResults.SystemError(request.requestId(), e.getMessage(), request.tracingId());
        }
    }
}
