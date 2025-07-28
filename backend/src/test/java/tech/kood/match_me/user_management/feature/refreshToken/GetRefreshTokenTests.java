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
import tech.kood.match_me.user_management.internal.features.refreshToken.getToken.GetRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.getToken.GetRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.getToken.GetRefreshTokenResults;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class GetRefreshTokenTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RegisterUserHandler registerUserHandler;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    CreateRefreshTokenHandler createRefreshTokenHandler;

    @Autowired
    GetRefreshTokenHandler getRefreshTokenRequestHandler;

    @Autowired
    @Qualifier("userManagementFlyway")
    Flyway userManagementFlyway;


    @Test
    void shouldGetRefreshTokenForValidUser() {
        var registerRequest = RegisterUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUserResults.Success;

        var user = ((RegisterUserResults.Success) registerResult).user();
        var createTokenRequest =
                new CreateRefreshTokenRequest(UUID.randomUUID(), user, Optional.empty());
        var createTokenResult = createRefreshTokenHandler.handle(createTokenRequest);

        assert createTokenResult instanceof CreateRefreshTokenResults.Success;

        var successResult = (CreateRefreshTokenResults.Success) createTokenResult;

        var getRefreshTokenRequest = new GetRefreshTokenRequest(UUID.randomUUID(),
                successResult.refreshToken().token(), Optional.empty());
        var getRefreshTokenResult = getRefreshTokenRequestHandler.handle(getRefreshTokenRequest);
        assert getRefreshTokenResult instanceof GetRefreshTokenResults.Success;

        var tokenResult = (GetRefreshTokenResults.Success) getRefreshTokenResult;
        assert tokenResult.token().token().equals(successResult.refreshToken().token());
        assert tokenResult.token().userId().equals(user.id());
    }
}
