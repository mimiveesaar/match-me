package tech.kood.match_me.user_management.features.refreshToken;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.actions.CreateRefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.actions.InvalidateRefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional(transactionManager = "userManagementTransactionManager")
public class InvalidateRefreshTokenTests extends UserManagementTestBase {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    CreateRefreshToken.Handler createRefreshTokenCommandHandler;

    @Autowired
    InvalidateRefreshToken.Handler invalidateRefreshTokenHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    void shouldInvalidateRefreshToken() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var userId = ((RegisterUser.Result.Success) registerResult).userId();
        var createTokenRequest = new CreateRefreshToken.Request(userId);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);

        assert createTokenResult instanceof CreateRefreshToken.Result.Success;

        var successResult = (CreateRefreshToken.Result.Success) createTokenResult;

        var invalidateRequest = new InvalidateRefreshToken.Request(successResult.refreshToken().secret());
        var invalidateResult = invalidateRefreshTokenHandler.handle(invalidateRequest);

        assert invalidateResult instanceof InvalidateRefreshToken.Result.Success;

        var tokenCheck = refreshTokenRepository.findToken(successResult.refreshToken().secret().toString());
        assert tokenCheck.isEmpty() : "The refresh token should be invalidated and not found in the repository";
    }
}