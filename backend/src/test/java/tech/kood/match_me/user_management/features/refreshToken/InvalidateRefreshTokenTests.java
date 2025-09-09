package tech.kood.match_me.user_management.features.refreshToken;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.InvalidateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.InvalidateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserResults;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class InvalidateRefreshTokenTests extends UserManagementTestBase {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    RegisterUserCommandHandler registerUserHandler;

    @Autowired
    CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;

    @Autowired
    InvalidateRefreshTokenCommandHandler invalidateRefreshTokenHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    void shouldInvalidateRefreshToken() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUserResults.Success;

        var userId = ((RegisterUserResults.Success) registerResult).userId();
        var createTokenRequest = new CreateRefreshTokenRequest(userId);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);

        assert createTokenResult instanceof CreateRefreshTokenResults.Success;

        var successResult = (CreateRefreshTokenResults.Success) createTokenResult;

        // Act: Invalidate the refresh token
        var invalidateRequest = new InvalidateRefreshTokenRequest(successResult.refreshToken().secret());
        var invalidateResult = invalidateRefreshTokenHandler.handle(invalidateRequest);

        // Assert: Check if the token was invalidated successfully
        assert invalidateResult instanceof InvalidateRefreshTokenResults.Success;

        var tokenCheck = refreshTokenRepository.findToken(successResult.refreshToken().secret().toString());
        assert tokenCheck.isEmpty() : "The refresh token should be invalidated and not found in the repository";
    }
}
