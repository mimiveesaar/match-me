package tech.kood.match_me.user_management.features.refreshToken;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api.GetRefreshTokenQueryHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api.GetRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api.GetRefreshTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class GetRefreshTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;

    @Autowired
    GetRefreshTokenQueryHandler getRefreshTokenRequestHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    void shouldGetRefreshTokenForValidUser() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var userId = ((RegisterUser.Result.Success) registerResult).userId();

        var createTokenRequest = new CreateRefreshTokenRequest(userId);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);

        assert createTokenResult instanceof CreateRefreshTokenResults.Success;
        var createdTokenResult = (CreateRefreshTokenResults.Success) createTokenResult;

        var getRefreshTokenRequest = new GetRefreshTokenRequest(createdTokenResult.refreshToken().secret());
        var getRefreshTokenResult = getRefreshTokenRequestHandler.handle(getRefreshTokenRequest);
        assert getRefreshTokenResult instanceof GetRefreshTokenResults.Success;

        var getTokenResult = (GetRefreshTokenResults.Success) getRefreshTokenResult;
        assert getTokenResult.refreshToken().equals(createdTokenResult.refreshToken());
    }
}