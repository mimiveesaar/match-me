package tech.kood.match_me.user_management.features.user.features.registerUser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserResults;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class RegisterUserTests extends UserManagementTestBase {

    @Autowired
    RegisterUserCommandHandler registerUserHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    void shouldCreateValidUser() throws CheckedConstraintViolationException {
        var request = registerUserRequestMocker.createValidRequest();
        var result = registerUserHandler.handle(request);
        System.out.println(result);
        assert result instanceof RegisterUserResults.Success;
    }

    @Test
    void shouldNotCreateUserWithExistingEmail() throws CheckedConstraintViolationException {
        var request = registerUserRequestMocker.createValidRequest();
        var request2 = registerUserRequestMocker.createValidRequest();
        request2 = request2.withEmail(request.email());
        registerUserHandler.handle(request);
        var result = registerUserHandler.handle(request2);
        System.out.println(result);
        assert result instanceof RegisterUserResults.EmailExists;
    }

    @Test
    void shouldNotCreateUserWithInvalidPassword() throws CheckedConstraintViolationException {
        var request = registerUserRequestMocker.createInvalidPasswordRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidRequest;
    }

    @Test
    void shouldNotCreateUserWithNullRequest() throws CheckedConstraintViolationException {
        var request = registerUserRequestMocker.createNullRequest();
        var result = registerUserHandler.handle(request);
        assert !(result instanceof RegisterUserResults.Success);
    }


    @Test
    void shouldNotCreateUserWithLongPassword() throws CheckedConstraintViolationException {
        var request = registerUserRequestMocker.createLongPasswordRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidRequest;
    }
}