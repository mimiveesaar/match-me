package tech.kood.match_me.user_management.internal.features.login;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.internal.common.cqrs.CommandHandler;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;
import tech.kood.match_me.user_management.internal.utils.PasswordUtils;

@Service
public final class LoginHandler implements CommandHandler<LoginRequest, LoginResults> {

    private final UserRepository userRepository;
    private final CreateRefreshTokenHandler createRefreshTokenHandler;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher events;

    public LoginHandler(UserRepository userRepository,
            CreateRefreshTokenHandler createRefreshTokenHandler, ApplicationEventPublisher events,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.createRefreshTokenHandler = createRefreshTokenHandler;
        this.userMapper = userMapper;
        this.events = events;
    }

    public LoginResults handle(LoginRequest request) {
        try {

            var userEntity = userRepository.findUserByEmail(request.getEmail());
            if (userEntity.isEmpty()) {
                var result = LoginResults.InvalidCredentials.of(request.getEmail(),
                        request.getPassword(), request.getRequestId(), request.getTracingId());
                events.publishEvent(new LoginEvent(request, result));
                return result;
            }

            var foundUser = userMapper.toUser(userEntity.get());

            if (!PasswordUtils.matches(request.getPassword(), foundUser.getHashedPassword())) {
                var result = LoginResults.InvalidCredentials.of(request.getEmail(),
                        request.getPassword(), request.getRequestId(), request.getTracingId());
                events.publishEvent(new LoginEvent(request, result));
                return result;
            }

            var refreshTokenRequest = CreateRefreshTokenRequest.of(UUID.randomUUID(), foundUser,
                    request.getTracingId());

            var tokenResult = createRefreshTokenHandler.handle(refreshTokenRequest);

            if (!(tokenResult instanceof CreateRefreshTokenResults.Success successResult)) {
                var result = LoginResults.SystemError.of("Failed to create refresh token.",
                        request.getRequestId(), request.getTracingId());
                events.publishEvent(new LoginEvent(request, result));
                return result;
            }

            var result = LoginResults.Success.of(successResult.getRefreshToken(), foundUser,
                    request.getRequestId(), request.getTracingId());
            events.publishEvent(new LoginEvent(request, result));
            return result;

        } catch (Exception e) {
            var result = LoginResults.SystemError.of(
                    "An unexpected error occurred during login: " + e.getMessage(),
                    request.getRequestId(), request.getTracingId());
            events.publishEvent(new LoginEvent(request, result));
            return result;
        }
    }
}
