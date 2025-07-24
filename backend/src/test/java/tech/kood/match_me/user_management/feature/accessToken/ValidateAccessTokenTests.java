package tech.kood.match_me.user_management.feature.accessToken;

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
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.jwt.getAccessToken.GetAccessTokenHandler;
import tech.kood.match_me.user_management.internal.features.jwt.getAccessToken.GetAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.jwt.getAccessToken.GetAccessTokenResults;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenResults;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
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
    GetAccessTokenHandler getAccessTokenHandler;

    @Autowired
    ValidateAccessTokenHandler validateAccessTokenHandler;

    @BeforeAll
    void migrate() {
        var result = userManagementFlyway.migrate();
    }

    @Test
    public void shouldValidateAccessToken() {
        var registerRequest = RegisterUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUserResults.Success;

        var user = ((RegisterUserResults.Success) registerResult).user();
        var createTokenRequest = new CreateRefreshTokenRequest(UUID.randomUUID(), user, Optional.empty());
        var createTokenResult = createRefreshTokenHandler.handle(createTokenRequest);
        assert createTokenResult instanceof CreateRefreshTokenResults.Success;

        var refreshToken = ((CreateRefreshTokenResults.Success) createTokenResult).refreshToken();
        var getAccessTokenRequest = new GetAccessTokenRequest(UUID.randomUUID(), refreshToken.token(),
                Optional.empty());
        var getAccessTokenResult = getAccessTokenHandler.handle(getAccessTokenRequest);

        assert getAccessTokenResult instanceof GetAccessTokenResults.Success;

        var jwt = ((GetAccessTokenResults.Success) getAccessTokenResult).jwt();
        assert jwt != null;

        var validateRequest = new ValidateAccessTokenRequest(UUID.randomUUID(), jwt, Optional.empty());
        var validateResult = validateAccessTokenHandler.handle(validateRequest);

        assert validateResult instanceof ValidateAccessTokenResults.Success;

        var userId = ((ValidateAccessTokenResults.Success) validateResult).accessToken().userId();
        assert userId != null;
    }

    @Test
    public void shouldHandleInvalidAccessToken() {
        var validateRequest = new ValidateAccessTokenRequest(UUID.randomUUID(), "invalid-token", Optional.empty());
        var validateResult = validateAccessTokenHandler.handle(validateRequest);

        assert validateResult instanceof ValidateAccessTokenResults.InvalidToken
                : "The handler should return an InvalidToken result for an invalid access token";
    }

    @Test
    public void shouldHandleMissingAccessToken() {
        var validateRequest = new ValidateAccessTokenRequest(UUID.randomUUID(), null, Optional.empty());
        var validateResult = validateAccessTokenHandler.handle(validateRequest);

        assert validateResult instanceof ValidateAccessTokenResults.InvalidRequest
                : "The handler should return an InvalidRequest result for a missing access token";
    }

}
