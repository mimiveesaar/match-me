package tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.internal.common.cqrs.CommandHandler;
import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.internal.domain.models.RefreshToken;
import tech.kood.match_me.user_management.internal.mappers.RefreshTokenMapper;

import java.util.UUID;

@Service
public class CreateRefreshTokenHandler
        implements CommandHandler<CreateRefreshTokenRequest, CreateRefreshTokenResults> {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenFactory refreshTokenFactory;
    private final ApplicationEventPublisher events;
    private final UserManagementConfig userManagementConfig;
    private final RefreshTokenMapper refreshTokenMapper;

    public CreateRefreshTokenHandler(RefreshTokenRepository refreshTokenRepository,
            ApplicationEventPublisher events, RefreshTokenFactory refreshTokenFactory,
            RefreshTokenMapper refreshTokenMapper, UserManagementConfig userManagementConfig) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.events = events;
        this.userManagementConfig = userManagementConfig;
        this.refreshTokenFactory = refreshTokenFactory;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    public CreateRefreshTokenResults handle(CreateRefreshTokenRequest request) {

        try {
            // We assume the user exists, as this is a refresh token operation.
            // Create a new refresh token
            RefreshToken refreshToken = this.refreshTokenFactory.create(request.getUser().id.value);
            var refreshTokenEntity = refreshTokenMapper.toEntity(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);

            var result = CreateRefreshTokenResults.Success.of(refreshToken,
                    UUID.fromString(request.requestId), request.tracingId);
            events.publishEvent(new CreateRefreshTokenEvent(request, result));
            return result;
        } catch (Exception e) {
            var result = CreateRefreshTokenResults.SystemError.of("An unexpected error occurred",
                    UUID.fromString(request.requestId), request.tracingId);
            events.publishEvent(new CreateRefreshTokenEvent(request, result));
            return result;
        }
    }
}
