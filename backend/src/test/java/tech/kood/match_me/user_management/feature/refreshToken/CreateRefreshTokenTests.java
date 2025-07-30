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
import tech.kood.match_me.user_management.internal.database.repostitories.RefreshTokenRepository;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateRefreshTokenTests extends UserManagementTestBase {

        @Autowired
        UserRepository userRepository;

        @Autowired
        RegisterUserHandler registerUserHandler;

        @Autowired
        RefreshTokenRepository refreshTokenRepository;

        @Autowired
        CreateRefreshTokenHandler createRefreshTokenHandler;

        @Autowired
        RegisterUserRequestMocker registerUserRequestMocker;

        @Autowired
        @Qualifier("userManagementFlyway")
        Flyway userManagementFlyway;


        @Test
        void shouldCreateRefreshTokenForValidUser() {
                var registerRequest = registerUserRequestMocker.createValidRequest();
                var registerResult = registerUserHandler.handle(registerRequest);
                assert registerResult instanceof RegisterUserResults.Success;

                var user = ((RegisterUserResults.Success) registerResult).user();
                var createTokenRequest = new CreateRefreshTokenRequest(UUID.randomUUID().toString(),
                                user, null);
                var createTokenResult = createRefreshTokenHandler.handle(createTokenRequest);

                assert createTokenResult instanceof CreateRefreshTokenResults.Success;

                var successResult = (CreateRefreshTokenResults.Success) createTokenResult;

                var token = refreshTokenRepository.findToken(successResult.refreshToken().token());
                assert token.isPresent() : "The refresh token should be created and found in the repository";
                assert token.get().userId().equals(
                                user.id()) : "The refresh token should belong to the correct user";
        }

        @Test
        void shouldHandleInvalidRequest() {
                var invalidRequest = new CreateRefreshTokenRequest(UUID.randomUUID().toString(),
                                null, null);
                var result = createRefreshTokenHandler.handle(invalidRequest);
                assert result instanceof CreateRefreshTokenResults.InvalidRequest : "The handler should return an InvalidRequest result for null user";
        }

}
