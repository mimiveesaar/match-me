package tech.kood.match_me.user_management.features.accessToken;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api.CreateAccessTokenCommandHandler;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api.CreateAccessTokenResults;
import tech.kood.match_me.user_management.features.accessToken.features.validateAccessToken.api.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.features.accessToken.features.validateAccessToken.api.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.features.validateAccessToken.api.ValidateAccessTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserResults;
import tech.kood.match_me.user_management.features.user.features.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ValidateAccessTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUserCommandHandler registerUserHandler;

    @Autowired
    CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;

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
        assert registerResult instanceof RegisterUserResults.Success;

        var userId = ((RegisterUserResults.Success) registerResult).userId();
        var createTokenRequest = new CreateRefreshTokenRequest(userId, null);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);
        assert createTokenResult instanceof CreateRefreshTokenResults.Success;

        var refreshToken = ((CreateRefreshTokenResults.Success) createTokenResult).refreshToken();
        var getAccessTokenRequest = new CreateAccessTokenRequest(refreshToken.secret(), null);
        var getAccessTokenResult = createAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof CreateAccessTokenResults.Success;

        var jwt = ((CreateAccessTokenResults.Success) getAccessTokenResult).jwt();
        assert jwt != null;

        var validateRequest = new ValidateAccessTokenRequest(jwt, null);
        var validateResult = validateAccessTokenHandler.handle(validateRequest);

        assert validateResult instanceof ValidateAccessTokenResults.Success;
    }

    @Test
    public void shouldHandleInvalidAccessToken() {
        var validateRequest = new ValidateAccessTokenRequest("invalid-token", null);
        var validateResult = validateAccessTokenHandler.handle(validateRequest);

        assert validateResult instanceof ValidateAccessTokenResults.InvalidToken : "The handler should return an InvalidToken result for an invalid access token";
    }
}
