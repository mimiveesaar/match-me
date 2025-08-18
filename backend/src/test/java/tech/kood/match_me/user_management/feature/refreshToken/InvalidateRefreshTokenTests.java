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
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.features.invalidateToken.api.InvalidateRefreshTokenHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.invalidateToken.api.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.invalidateToken.api.InvalidateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserResults;
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
        CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;

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
                var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);

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
