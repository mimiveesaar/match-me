package tech.kood.match_me.user_management.feature.accessToken;

import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.internal.features.jwt.createAccessToken.CreateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.features.jwt.createAccessToken.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.jwt.createAccessToken.CreateAccessTokenResults;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ValidateAccessTokenTests extends UserManagementTestBase {

        @Autowired
        UserRepository userRepository;

        @Autowired
        @Qualifier("userManagementFlyway")
        Flyway userManagementFlyway;

        @Autowired
        RegisterUserCommandHandler registerUserHandler;

        @Autowired
        CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;

        @Autowired
        CreateAccessTokenHandler getAccessTokenHandler;

        @Autowired
        ValidateAccessTokenHandler validateAccessTokenHandler;

        @Autowired
        RegisterUserRequestMocker registerUserRequestMocker;

        @Test
        public void shouldValidateAccessToken() {
                var registerRequest = registerUserRequestMocker.createValidRequest();
                var registerResult = registerUserHandler.handle(registerRequest);
                assert registerResult instanceof RegisterUserResults.Success;

                var user = ((RegisterUserResults.Success) registerResult).user();
                var createTokenRequest =
                                CreateRefreshTokenRequest.of(UUID.randomUUID(), user, null);
                var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);
                assert createTokenResult instanceof CreateRefreshTokenResults.Success;

                var refreshToken = ((CreateRefreshTokenResults.Success) createTokenResult)
                                .refreshToken();
                var getAccessTokenRequest = CreateAccessTokenRequest.of(UUID.randomUUID(),
                                refreshToken.getToken(), null);
                var getAccessTokenResult = getAccessTokenHandler.handle(getAccessTokenRequest);

                assert getAccessTokenResult instanceof CreateAccessTokenResults.Success;

                var jwt = ((CreateAccessTokenResults.Success) getAccessTokenResult).getJwt();
                assert jwt != null;

                var validateRequest = ValidateAccessTokenRequest.of(UUID.randomUUID(), jwt, null);
                var validateResult = validateAccessTokenHandler.handle(validateRequest);

                assert validateResult instanceof ValidateAccessTokenResults.Success;

                var userId = ((ValidateAccessTokenResults.Success) validateResult).getAccessToken()
                                .getUserId();
                assert userId != null;
        }

        @Test
        public void shouldHandleInvalidAccessToken() {
                var validateRequest = ValidateAccessTokenRequest.of(UUID.randomUUID(),
                                "invalid-token", null);
                var validateResult = validateAccessTokenHandler.handle(validateRequest);

                assert validateResult instanceof ValidateAccessTokenResults.InvalidToken : "The handler should return an InvalidToken result for an invalid access token";
        }
}
