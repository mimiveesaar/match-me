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
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.domain.features.jwt.createAccessToken.CreateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.jwt.createAccessToken.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.jwt.createAccessToken.CreateAccessTokenResults;
import tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken.ValidateAccessTokenResults;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken.CreateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.domain.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.domain.features.registerUser.RegisterUserResults;
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
        RegisterUserHandler registerUserHandler;

        @Autowired
        CreateRefreshTokenHandler createRefreshTokenHandler;

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
                var createTokenRequest = new CreateRefreshTokenRequest(UUID.randomUUID().toString(),
                                user, null);
                var createTokenResult = createRefreshTokenHandler.handle(createTokenRequest);
                assert createTokenResult instanceof CreateRefreshTokenResults.Success;

                var refreshToken = ((CreateRefreshTokenResults.Success) createTokenResult)
                                .refreshToken();
                var getAccessTokenRequest = CreateAccessTokenRequest.of(UUID.randomUUID(),
                                refreshToken.token(), null);
                var getAccessTokenResult = getAccessTokenHandler.handle(getAccessTokenRequest);

                assert getAccessTokenResult instanceof CreateAccessTokenResults.Success;

                var jwt = ((CreateAccessTokenResults.Success) getAccessTokenResult).jwt;
                assert jwt != null;

                var validateRequest = ValidateAccessTokenRequest.of(UUID.randomUUID(),
                                jwt, null);
                var validateResult = validateAccessTokenHandler.handle(validateRequest);

                assert validateResult instanceof ValidateAccessTokenResults.Success;

                var userId = ((ValidateAccessTokenResults.Success) validateResult).accessToken.userId();
                assert userId != null;
        }

        @Test
        public void shouldHandleInvalidAccessToken() {
                var validateRequest = ValidateAccessTokenRequest.of(UUID.randomUUID(),
                                "invalid-token", null);
                var validateResult = validateAccessTokenHandler.handle(validateRequest);

                assert validateResult instanceof ValidateAccessTokenResults.InvalidToken : "The handler should return an InvalidToken result for an invalid access token";
        }

        @Test
        public void shouldHandleMissingAccessToken() {
                var validateRequest = ValidateAccessTokenRequest.of(UUID.randomUUID(),
                                null, null);
                var validateResult = validateAccessTokenHandler.handle(validateRequest);

                assert validateResult instanceof ValidateAccessTokenResults.InvalidRequest : "The handler should return an InvalidRequest result for a missing access token";
        }

}
