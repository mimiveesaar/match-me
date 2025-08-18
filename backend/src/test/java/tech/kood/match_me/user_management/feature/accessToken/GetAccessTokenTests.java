package tech.kood.match_me.user_management.feature.accessToken;

import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.CreateAccessTokenHandler;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.CreateAccessTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class GetAccessTokenTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway")
    Flyway userManagementFlyway;

    @Autowired
    RegisterUserCommandHandler registerUserHandler;

    @Autowired
    CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;

    @Autowired
    CreateAccessTokenHandler getAccessTokenHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;


    @Test
    public void shouldGetAccessTokenForValidRefreshToken() {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUserResults.Success;

        var user = ((RegisterUserResults.Success) registerResult).user();
        var createTokenRequest = CreateRefreshTokenRequest.of(UUID.randomUUID(), user, null);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);
        assert createTokenResult instanceof CreateRefreshTokenResults.Success;

        var refreshToken =
                ((CreateRefreshTokenResults.Success) createTokenResult).refreshToken();
        var getAccessTokenRequest =
                CreateAccessTokenRequest.of(UUID.randomUUID(), refreshToken.getToken(), null);
        var getAccessTokenResult = getAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof CreateAccessTokenResults.Success;

        var accessToken = ((CreateAccessTokenResults.Success) getAccessTokenResult).jwt();
        assert accessToken != null;
    }

    @Test
    public void shouldHandleInvalidRefreshToken() {
        var getAccessTokenRequest =
                CreateAccessTokenRequest.of(UUID.randomUUID(), "invalid-token", null);
        var getAccessTokenResult = getAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof CreateAccessTokenResults.InvalidToken : "The handler should return an InvalidToken result for an invalid refresh token";
    }
}
