package tech.kood.match_me.user_management.features.user.actions.login;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.actions.LoginUser;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional(transactionManager = "userManagementTransactionManager")
public class LoginTests extends UserManagementTestBase {

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    LoginUser.Handler loginRequestHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    void shouldLoginWithValidCredentials() throws CheckedConstraintViolationException {

        var registerUserRequest = registerUserRequestMocker.createValidRequest();

        var registerResult = registerUserHandler.handle(registerUserRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var loginRequest = new LoginUser.Request(registerUserRequest.email(), registerUserRequest.password());

        var loginResult = loginRequestHandler.handle(loginRequest);
        assert loginResult instanceof LoginUser.Result.Success;
    }
}