package tech.kood.match_me.user_management.internal.features.refreshToken.createToken;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.features.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.internal.mappers.RefreshTokenMapper;

@Service
public class CreateRefreshTokenHandler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenFactory refreshTokenFactory;
    private final ApplicationEventPublisher events;
    private final UserManagementConfig userManagementConfig;
    private final RefreshTokenMapper refreshTokenMapper;

    public CreateRefreshTokenHandler(
            RefreshTokenRepository refreshTokenRepository,
            ApplicationEventPublisher events,
            RefreshTokenFactory refreshTokenFactory,
            RefreshTokenMapper refreshTokenMapper,
            UserManagementConfig userManagementConfig) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.events = events;
        this.userManagementConfig = userManagementConfig;
        this.refreshTokenFactory = refreshTokenFactory;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    public CreateRefreshTokenResults handle(CreateRefreshTokenRequest request) {

        try {
            // Validate the request
            if (request.user() == null) {
                var result = new CreateRefreshTokenResults.InvalidRequest("User ID must not be null", request.tracingId());
                events.publishEvent(new CreateRefreshTokenEvent(request, result));
                return result;
            }

            if (request.user().toString().isBlank()) {
                var result = new CreateRefreshTokenResults.InvalidRequest("User ID must not be blank", request.tracingId());
                events.publishEvent(new CreateRefreshTokenEvent(request, result));
                return result;
            }

            // We assume the user exists, as this is a refresh token operation.
            // Create a new refresh token
            var refreshToken = this.refreshTokenFactory.create(request.user().id());
            var refreshTokenEntity = refreshTokenMapper.toEntity(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);

            var result = new CreateRefreshTokenResults.Success(refreshToken);
            events.publishEvent(new CreateRefreshTokenEvent(request, result));
            return result;
        } catch (Exception e) {
            var result = new CreateRefreshTokenResults.SystemError("An unexpected error occurred", request.tracingId());
            events.publishEvent(new CreateRefreshTokenEvent(request, result));
            return result;
        }
    }
}
