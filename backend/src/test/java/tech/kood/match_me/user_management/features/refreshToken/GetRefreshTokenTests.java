package tech.kood.match_me.user_management.features.refreshToken;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.actions.CreateRefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.actions.GetRefreshToken;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class GetRefreshTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    CreateRefreshToken.Handler createRefreshTokenCommandHandler;

    @Autowired
    GetRefreshToken.Handler getRefreshTokenRequestHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    void shouldGetRefreshTokenForValidUser() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var userId = ((RegisterUser.Result.Success) registerResult).userId();

        var createTokenRequest = new CreateRefreshToken.Request(userId);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);

        assert createTokenResult instanceof CreateRefreshToken.Result.Success;
        var createdTokenResult = (CreateRefreshToken.Result.Success) createTokenResult;

        var getRefreshTokenRequest = new GetRefreshToken.Request(createdTokenResult.refreshToken().secret());
        var getRefreshTokenResult = getRefreshTokenRequestHandler.handle(getRefreshTokenRequest);
        assert getRefreshTokenResult instanceof GetRefreshToken.Result.Success;

        var getTokenResult = (GetRefreshToken.Result.Success) getRefreshTokenResult;
        assert getTokenResult.refreshToken().equals(createdTokenResult.refreshToken());
    }
}