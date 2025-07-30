package tech.kood.match_me.user_management.internal.features.login;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;
import tech.kood.match_me.user_management.internal.utils.PasswordUtils;

@Service
public final class LoginHandler {

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
            if (request.email() == null || request.password() == null) {

                var result = new LoginResults.InvalidRequest("Email and password must not be null",
                        request.tracingId());

                events.publishEvent(new LoginEvent(request, result));
                return result;
            }

            if (request.email().isBlank() || request.password().isBlank()) {
                var result = new LoginResults.InvalidRequest("Email and password cannot be empty.",
                        request.tracingId());

                events.publishEvent(new LoginEvent(request, result));
                return result;
            }

            var userEntity = userRepository.findUserByEmail(request.email());
            if (userEntity.isEmpty()) {
                var result =
                        new LoginResults.InvalidCredentials(request.email(), request.password());

                events.publishEvent(new LoginEvent(request, result));
                return result;
            }

            var foundUser = userMapper.toUser(userEntity.get());

            // Check if the provided password matches the stored hashed password.
            if (!PasswordUtils.matches(request.password(), foundUser.password())) {
                var result =
                        new LoginResults.InvalidCredentials(request.email(), request.password());

                events.publishEvent(new LoginEvent(request, result));
                return result;
            }

            var refreshTokenRequest = new CreateRefreshTokenRequest(UUID.randomUUID().toString(),
                    foundUser, request.tracingId());

            var tokenResult = createRefreshTokenHandler.handle(refreshTokenRequest);

            if (!(tokenResult instanceof CreateRefreshTokenResults.Success)) {
                var result = new LoginResults.SystemError("Failed to create refresh token.",
                        request.tracingId());
                events.publishEvent(new LoginEvent(request, result));
                return result;
            }

            var result = new LoginResults.Success(
                    ((CreateRefreshTokenResults.Success) tokenResult).refreshToken(), foundUser,
                    request.tracingId());
            events.publishEvent(new LoginEvent(request, result));
            return result;

        } catch (Exception e) {
            var result = new LoginResults.SystemError(
                    "An unexpected error occurred during login: " + e.getMessage(),
                    request.tracingId());
            events.publishEvent(new LoginEvent(request, result));
            return result;
        }
    }
}
