package tech.kood.match_me.user_management.features.accessToken;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenCommandHandler;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenResults;
import tech.kood.match_me.user_management.features.accessToken.actions.validateAccessToken.api.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.features.accessToken.actions.validateAccessToken.api.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.actions.validateAccessToken.api.ValidateAccessTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.actions.CreateRefreshToken;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ValidateAccessTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    CreateRefreshToken.Handler createRefreshTokenCommandHandler;

    @Autowired
    CreateAccessTokenCommandHandler createAccessTokenHandler;

    @Autowired
    ValidateAccessTokenHandler validateAccessTokenHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    public void shouldValidateAccessToken() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var userId = ((RegisterUser.Result.Success) registerResult).userId();
        var createTokenRequest = new CreateRefreshToken.Request(userId);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);
        assert createTokenResult instanceof CreateRefreshToken.Result.Success;

        var refreshToken = ((CreateRefreshToken.Result.Success) createTokenResult).refreshToken();
        var getAccessTokenRequest = new CreateAccessTokenRequest(refreshToken.secret());
        var getAccessTokenResult = createAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof CreateAccessTokenResults.Success;

        var jwt = ((CreateAccessTokenResults.Success) getAccessTokenResult).jwt();
        assert jwt != null;

        var validateRequest = new ValidateAccessTokenRequest(jwt);
        var validateResult = validateAccessTokenHandler.handle(validateRequest);

        assert validateResult instanceof ValidateAccessTokenResults.Success;
    }

    @Test
    public void shouldHandleInvalidAccessToken() {
        var validateRequest = new ValidateAccessTokenRequest("invalid-token");
        var validateResult = validateAccessTokenHandler.handle(validateRequest);

        assert validateResult instanceof ValidateAccessTokenResults.InvalidToken : "The handler should return an InvalidToken result for an invalid access token";
    }
}
