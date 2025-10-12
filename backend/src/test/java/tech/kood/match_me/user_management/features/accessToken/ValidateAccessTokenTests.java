package tech.kood.match_me.user_management.features.accessToken;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.accessToken.actions.CreateAccessToken;
import tech.kood.match_me.user_management.features.accessToken.actions.ValidateAccessToken;
import tech.kood.match_me.user_management.features.refreshToken.actions.CreateRefreshToken;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@Transactional(transactionManager = "userManagementTransactionManager")
public class ValidateAccessTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    CreateRefreshToken.Handler createRefreshTokenCommandHandler;

    @Autowired
    CreateAccessToken.Handler createAccessTokenHandler;

    @Autowired
    ValidateAccessToken.Handler validateAccessTokenHandler;

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
        var getAccessTokenRequest = new CreateAccessToken.Request(refreshToken.secret());
        var getAccessTokenResult = createAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof CreateAccessToken.Result.Success;

        var jwt = ((CreateAccessToken.Result.Success) getAccessTokenResult).jwt();
        assert jwt != null;

        var validateRequest = new ValidateAccessToken.Request(jwt);
        var validateResult = validateAccessTokenHandler.handle(validateRequest);

        assert validateResult instanceof ValidateAccessToken.Result.Success;
    }

    @Test
    public void shouldHandleInvalidAccessToken() {
        var validateRequest = new ValidateAccessToken.Request("invalid-token");
        var validateResult = validateAccessTokenHandler.handle(validateRequest);

        assert validateResult instanceof ValidateAccessToken.Result.InvalidToken : "The handler should return an InvalidToken result for an invalid access token";
    }
}