package tech.kood.match_me.user_management.features.accessToken;

import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api.CreateAccessTokenCommandHandler;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api.CreateAccessTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserResults;
import tech.kood.match_me.user_management.features.user.features.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class GetAccessTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUserCommandHandler registerUserHandler;

    @Autowired
    CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;

    @Autowired
    CreateAccessTokenCommandHandler getAccessTokenHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;


    @Test
    public void shouldGetAccessTokenForValidRefreshToken() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUserResults.Success;

        var user = ((RegisterUserResults.Success) registerResult).userId();
        var createTokenRequest = new CreateRefreshTokenRequest(user, null);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);
        assert createTokenResult instanceof CreateRefreshTokenResults.Success;

        var refreshToken = ((CreateRefreshTokenResults.Success) createTokenResult).refreshToken();
        var getAccessTokenRequest = new CreateAccessTokenRequest(refreshToken.secret(), null);
        var getAccessTokenResult = getAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof CreateAccessTokenResults.Success;

        var accessToken = ((CreateAccessTokenResults.Success) getAccessTokenResult).jwt();
        assert accessToken != null;
    }

    @Test
    public void shouldHandleInvalidRefreshToken() {

        //Create a request with an invalid refresh token.
        var getAccessTokenRequest = new CreateAccessTokenRequest(new RefreshTokenSecretDTO(UUID.randomUUID()), null);
        var getAccessTokenResult = getAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof CreateAccessTokenResults.InvalidToken : "The handler should return an InvalidToken result for an invalid refresh token";
    }
}
