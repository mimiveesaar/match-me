package tech.kood.match_me.user_management.features.user.features.login.internal;

import jakarta.validation.Validator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.internal.password.PasswordFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword.HashedPasswordFactory;
import tech.kood.match_me.user_management.features.user.features.login.api.LoginCommandHandler;
import tech.kood.match_me.user_management.features.user.features.login.api.LoginRequest;
import tech.kood.match_me.user_management.features.user.features.login.api.LoginResults;
import tech.kood.match_me.user_management.features.user.features.login.api.UserLoggedInEvent;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenResults;

@Service
public final class LoginCommandHandlerImpl implements LoginCommandHandler {

    private final UserRepository userRepository;
    private final CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;
    private final ApplicationEventPublisher events;
    private final HashedPasswordFactory hashedPasswordFactory;
    private final PasswordFactory passwordFactory;
    private final UserMapper userMapper;
    private final Validator validator;

    public LoginCommandHandlerImpl(UserRepository userRepository,
                                   CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler,
                                   ApplicationEventPublisher events, HashedPasswordFactory hashedPasswordFactory, PasswordFactory passwordFactory, UserMapper userMapper, Validator validator) {
        this.userRepository = userRepository;
        this.createRefreshTokenCommandHandler = createRefreshTokenCommandHandler;
        this.events = events;
        this.hashedPasswordFactory = hashedPasswordFactory;
        this.passwordFactory = passwordFactory;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    @Override
    public LoginResults handle(LoginRequest request) {

        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new LoginResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.from(validationResults), request.tracingId());
        }

        try {
            var userEntity = userRepository.findUserByEmail(request.email().value());
            if (userEntity.isEmpty()) {
                return new LoginResults.InvalidCredentials(request.requestId(), request.tracingId());
            }

            var foundUser = userMapper.toUser(userEntity.get());

            var password = passwordFactory.create(request.password().value());
            var hashedPassword = hashedPasswordFactory.fromPlainText(password, foundUser.getHashedPassword().getSalt());

            if (!foundUser.getHashedPassword().equals(hashedPassword)) {
                return new LoginResults.InvalidCredentials(request.requestId(), request.tracingId());
            }

            var foundUserDTO = userMapper.toDTO(foundUser);
            var refreshTokenRequest = new CreateRefreshTokenRequest(foundUserDTO.id(), request.tracingId());
            var tokenResult = createRefreshTokenCommandHandler.handle(refreshTokenRequest);

            if (tokenResult instanceof CreateRefreshTokenResults.SystemError systemError) {
                //In a real application we would retry.
                return new LoginResults.SystemError(request.requestId(), systemError.message(), request.tracingId());
            }

            if (!(tokenResult instanceof CreateRefreshTokenResults.Success successResult)) {
                //This should never happen.
                return new LoginResults.SystemError(request.requestId(), "Unexpected result from refresh token handler", request.tracingId());
            }

            events.publishEvent(new UserLoggedInEvent(foundUserDTO.id()));
            return new LoginResults.Success(request.requestId(), successResult.refreshToken(), request.tracingId());

        } catch (Exception e) {
            return new LoginResults.SystemError(request.requestId(), e.getMessage(), request.tracingId());
        }
    }
}
