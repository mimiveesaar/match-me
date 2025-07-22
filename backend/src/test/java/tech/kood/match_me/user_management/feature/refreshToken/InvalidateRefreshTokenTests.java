package tech.kood.match_me.user_management.feature.refreshToken;

import java.util.Optional;
import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken.InvalidateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken.InvalidateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class InvalidateRefreshTokenTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    @Qualifier("userManagementFlyway")
    Flyway userManagementFlyway;

    @Autowired
    RegisterUserHandler registerUserHandler;

    @Autowired
    CreateRefreshTokenHandler createRefreshTokenHandler;

    @Autowired
    InvalidateRefreshTokenHandler invalidateRefreshTokenHandler;

    @BeforeAll
    void migrate() {
        var result = userManagementFlyway.migrate();
    }

    @Test
    void shouldInvalidateRefreshToken() {
        var registerRequest = RegisterUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUserResults.Success;

        var user = ((RegisterUserResults.Success) registerResult).user();
        var createTokenRequest = new CreateRefreshTokenRequest(UUID.randomUUID(), user, Optional.empty());
        var createTokenResult = createRefreshTokenHandler.handle(createTokenRequest);

        assert createTokenResult instanceof CreateRefreshTokenResults.Success;

        var successResult = (CreateRefreshTokenResults.Success) createTokenResult;

        // Act: Invalidate the refresh token
        var invalidateRequest = new InvalidateRefreshTokenRequest(UUID.randomUUID(),
                successResult.refreshToken().token(), Optional.empty());
        var invalidateResult = invalidateRefreshTokenHandler.handle(invalidateRequest);

        // Assert: Check if the token was invalidated successfully
        assert invalidateResult instanceof InvalidateRefreshTokenResults.Success;

        var tokenCheck = refreshTokenRepository.findToken(successResult.refreshToken().token());
        assert tokenCheck.isEmpty() : "The refresh token should be invalidated and not found in the repository";
    }
}
