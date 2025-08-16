package tech.kood.match_me.user_management.feature.refreshToken;

import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.features.refreshToken.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.features.user.persistance.UserRepository;
import tech.kood.match_me.user_management.internal.features.refreshToken.features.createToken.CreateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.features.createToken.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.features.createToken.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.features.refreshToken.features.invalidateToken.InvalidateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.features.invalidateToken.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.features.invalidateToken.InvalidateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.features.user.features.registerUser.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.internal.features.user.features.registerUser.RegisterUserResults;
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
        RegisterUserCommandHandler registerUserHandler;

        @Autowired
        CreateRefreshTokenHandler createRefreshTokenHandler;

        @Autowired
        InvalidateRefreshTokenHandler invalidateRefreshTokenHandler;

        @Autowired
        RegisterUserRequestMocker registerUserRequestMocker;

        @Test
        void shouldInvalidateRefreshToken() {
                var registerRequest = registerUserRequestMocker.createValidRequest();
                var registerResult = registerUserHandler.handle(registerRequest);
                assert registerResult instanceof RegisterUserResults.Success;

                var user = ((RegisterUserResults.Success) registerResult).user();
                var createTokenRequest =
                                CreateRefreshTokenRequest.of(UUID.randomUUID(), user, null);
                var createTokenResult = createRefreshTokenHandler.handle(createTokenRequest);

                assert createTokenResult instanceof CreateRefreshTokenResults.Success;

                var successResult = (CreateRefreshTokenResults.Success) createTokenResult;

                // Act: Invalidate the refresh token
                var invalidateRequest = InvalidateRefreshTokenRequest.of(UUID.randomUUID(),
                                successResult.refreshToken().getToken(), null);
                var invalidateResult = invalidateRefreshTokenHandler.handle(invalidateRequest);

                // Assert: Check if the token was invalidated successfully
                assert invalidateResult instanceof InvalidateRefreshTokenResults.Success;

                var tokenCheck = refreshTokenRepository
                                .findToken(successResult.refreshToken().getToken());
                assert tokenCheck
                                .isEmpty() : "The refresh token should be invalidated and not found in the repository";
        }
}
