package tech.kood.match_me.user_management.features.user.actions.login.internal;

import jakarta.validation.Validator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.internal.password.PasswordFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.hashedPassword.HashedPasswordFactory;
import tech.kood.match_me.user_management.features.user.actions.login.api.LoginCommandHandler;
import tech.kood.match_me.user_management.features.user.actions.login.api.LoginRequest;
import tech.kood.match_me.user_management.features.user.actions.login.api.LoginResults;
import tech.kood.match_me.user_management.features.user.actions.login.api.UserLoggedInEvent;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenResults;

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
            return new LoginResults.InvalidRequest(InvalidInputErrorDTO.from(validationResults));
        }

        try {
            var userEntity = userRepository.findUserByEmail(request.email().value());
            if (userEntity.isEmpty()) {
                return new LoginResults.InvalidCredentials();
            }

            var foundUser = userMapper.toUser(userEntity.get());

            var password = passwordFactory.create(request.password().value());
            var hashedPassword = hashedPasswordFactory.fromPlainText(password, foundUser.getHashedPassword().getSalt());

            if (!foundUser.getHashedPassword().equals(hashedPassword)) {
                return new LoginResults.InvalidCredentials();
            }

            var foundUserDTO = userMapper.toDTO(foundUser);
            var refreshTokenRequest = new CreateRefreshTokenRequest(foundUserDTO.id());
            var tokenResult = createRefreshTokenCommandHandler.handle(refreshTokenRequest);

            if (tokenResult instanceof CreateRefreshTokenResults.SystemError systemError) {
                //In a real application we would retry.
                return new LoginResults.SystemError(systemError.message());
            }

            if (!(tokenResult instanceof CreateRefreshTokenResults.Success successResult)) {
                //This should never happen.
                return new LoginResults.SystemError("Unexpected result from refresh token handler");
            }

            events.publishEvent(new UserLoggedInEvent(foundUserDTO.id()));
            return new LoginResults.Success(successResult.refreshToken());

        } catch (Exception e) {
            return new LoginResults.SystemError(e.getMessage());
        }
    }
}
