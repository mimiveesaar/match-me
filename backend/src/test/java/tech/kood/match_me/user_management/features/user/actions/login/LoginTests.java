package tech.kood.match_me.user_management.features.user.actions.login;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.actions.login.api.LoginCommandHandler;
import tech.kood.match_me.user_management.features.user.actions.login.api.LoginRequest;
import tech.kood.match_me.user_management.features.user.actions.login.api.LoginResults;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserResults;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class LoginTests extends UserManagementTestBase {

    @Autowired
    RegisterUserCommandHandler registerUserHandler;

    @Autowired
    LoginCommandHandler loginRequestHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    void shouldLoginWithValidCredentials() throws CheckedConstraintViolationException {

        var registerUserRequest = registerUserRequestMocker.createValidRequest();

        var registerResult = registerUserHandler.handle(registerUserRequest);
        assert registerResult instanceof RegisterUserResults.Success;

        var loginRequest = new LoginRequest(registerUserRequest.email(),
                registerUserRequest.password());

        var loginResult = loginRequestHandler.handle(loginRequest);
        assert loginResult instanceof LoginResults.Success;
    }
}
