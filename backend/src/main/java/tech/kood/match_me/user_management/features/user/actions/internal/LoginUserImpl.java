package tech.kood.match_me.user_management.features.user.actions.internal;



import jakarta.validation.Validator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.internal.password.PasswordFactory;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;
import tech.kood.match_me.user_management.features.user.actions.LoginUser;
import tech.kood.match_me.user_management.features.user.domain.internal.hashedPassword.HashedPasswordFactory;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenResults;

@Service
public class LoginUserImpl implements LoginUser.Handler {

    private final UserRepository userRepository;
    private final CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;
    private final CreateAccessTokenCommandHandler createAccessTokenCommandHandler;
    private final ApplicationEventPublisher events;
    private final HashedPasswordFactory hashedPasswordFactory;
    private final PasswordFactory passwordFactory;
    private final UserMapper userMapper;
    private final Validator validator;

    public LoginUserImpl(UserRepository userRepository,
                         CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler, CreateAccessTokenCommandHandler createAccessTokenCommandHandler,
                         ApplicationEventPublisher events, HashedPasswordFactory hashedPasswordFactory, PasswordFactory passwordFactory, UserMapper userMapper, Validator validator) {
        this.userRepository = userRepository;
        this.createRefreshTokenCommandHandler = createRefreshTokenCommandHandler;
        this.createAccessTokenCommandHandler = createAccessTokenCommandHandler;
        this.events = events;
        this.hashedPasswordFactory = hashedPasswordFactory;
        this.passwordFactory = passwordFactory;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    @Override
    @Transactional
    public LoginUser.Result handle(LoginUser.Request request) {

        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new LoginUser.Result.InvalidRequest(InvalidInputErrorDTO.from(validationResults));
        }

        try {
            var userEntity = userRepository.findUserByEmail(request.email().value());
            if (userEntity.isEmpty()) {
                return new LoginUser.Result.InvalidCredentials();
            }

            var foundUser = userMapper.toUser(userEntity.get());

            var password = passwordFactory.create(request.password().value());
            var hashedPassword = hashedPasswordFactory.fromPlainText(password, foundUser.getHashedPassword().getSalt());

            if (!foundUser.getHashedPassword().equals(hashedPassword)) {
                return new LoginUser.Result.InvalidCredentials();
            }

            var foundUserDTO = userMapper.toDTO(foundUser);
            var refreshTokenRequest = new CreateRefreshTokenRequest(foundUserDTO.id());
            var refreshTokenResult = createRefreshTokenCommandHandler.handle(refreshTokenRequest);

            if (refreshTokenResult instanceof CreateRefreshTokenResults.SystemError(String message)) {
                //In a real application we would retry.
                return new LoginUser.Result.SystemError(message);
            }

            if (!(refreshTokenResult instanceof CreateRefreshTokenResults.Success(
                    RefreshTokenDTO refreshToken
            ))) {
                //This should never happen.
                return new LoginUser.Result.SystemError("Unexpected result from refresh token handler");
            }

            events.publishEvent(new LoginUser.UserLoggedIn(foundUserDTO.id()));
            return new LoginUser.Result.Success(refreshToken, null);

        } catch (Exception e) {
            return new LoginUser.Result.SystemError(e.getMessage());
        }
    }
}