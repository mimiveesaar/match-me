package tech.kood.match_me.user_management.feature.login;

import java.util.Optional;
import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.login.LoginRequest;
import tech.kood.match_me.user_management.internal.features.login.LoginHandler;
import tech.kood.match_me.user_management.internal.features.login.LoginResults;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class LoginRequestTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway")
    Flyway userManagementFlyway;

    @Autowired
    RegisterUserHandler registerUserHandler;

    @Autowired
    LoginHandler loginRequestHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    void shouldLoginWithValidCredentials() {

        var registerUserRequest = registerUserRequestMocker.createValidRequest();

        var registerResult = registerUserHandler.handle(registerUserRequest);
        assert registerResult instanceof RegisterUserResults.Success;

        var loginRequest = new LoginRequest(UUID.randomUUID(), registerUserRequest.email(),
                registerUserRequest.password(), Optional.empty());

        var loginResult = loginRequestHandler.handle(loginRequest);
        assert loginResult instanceof LoginResults.Success;
    }
}
