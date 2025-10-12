package tech.kood.match_me.user_management.features.accessToken;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.accessToken.actions.CreateAccessToken;
import tech.kood.match_me.user_management.features.refreshToken.actions.CreateRefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@Transactional(transactionManager = "userManagementTransactionManager")
public class GetAccessTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    CreateRefreshToken.Handler createRefreshTokenCommandHandler;

    @Autowired
    CreateAccessToken.Handler getAccessTokenHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;


    @Test
    public void shouldGetAccessTokenForValidRefreshToken() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var user = ((RegisterUser.Result.Success) registerResult).userId();
        var createTokenRequest = new CreateRefreshToken.Request(user);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);
        assert createTokenResult instanceof CreateRefreshToken.Result.Success;

        var refreshToken = ((CreateRefreshToken.Result.Success) createTokenResult).refreshToken();
        var getAccessTokenRequest = new CreateAccessToken.Request(refreshToken.secret());
        var getAccessTokenResult = getAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof CreateAccessToken.Result.Success;

        var accessToken = ((CreateAccessToken.Result.Success) getAccessTokenResult).jwt();
        assert accessToken != null;
    }

    @Test
    public void shouldHandleInvalidRefreshToken() {

        //Create a request with an invalid refresh token.
        var getAccessTokenRequest = new CreateAccessToken.Request(new RefreshTokenSecretDTO(UUID.randomUUID()));
        var getAccessTokenResult = getAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof CreateAccessToken.Result.InvalidToken : "The handler should return an InvalidToken result for an invalid refresh token";
    }
}