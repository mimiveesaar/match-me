package tech.kood.match_me.user_management.features.user.actions.getUser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.TestInstance;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.*;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class GetUserTests extends UserManagementTestBase {

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    GetUserByEmailQueryHandler getUserByEmailHandler;

    @Autowired
    GetUserByIdQueryHandler getUserByIdHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;


    @Test
    void shouldGetUserById() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var userId = ((RegisterUser.Result.Success) registerResult).userId();

        var getRequest = new GetUserByIdRequest(userId);

        var getResult = getUserByIdHandler.handle(getRequest);
        assert getResult instanceof GetUserByIdResults.Success;
    }

    @Test
    void shouldGetUserByEmail() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var email = registerRequest.email();
        var getRequest = new GetUserByEmailRequest(email);

        var getResult = getUserByEmailHandler.handle(getRequest);
        assert getResult instanceof GetUserByEmailResults.Success;
    }

    @Test
    void shouldReturnUserNotFoundForInvalidId() {
        var getRequest = new GetUserByIdRequest(new UserIdDTO(UUID.randomUUID()));

        var getResult = getUserByIdHandler.handle(getRequest);
        assert getResult instanceof GetUserByIdResults.UserNotFound;
    }

    @Test
    void shouldReturnUserNotFoundForInvalidEmail() {
        var getRequest = new GetUserByEmailRequest(new EmailDTO("void@example.com"));
        var getResult = getUserByEmailHandler.handle(getRequest);
        assert getResult instanceof GetUserByEmailResults.UserNotFound;
    }

    @Test
    void shouldGetUserByEmailAfterRegisteringMultipleUsers() throws CheckedConstraintViolationException {
        var req1 = registerUserRequestMocker.createValidRequest();
        var req2 = registerUserRequestMocker.createValidRequest();
        var res1 = registerUserHandler.handle(req1);
        var res2 = registerUserHandler.handle(req2);
        assert res1 instanceof RegisterUser.Result.Success;
        assert res2 instanceof RegisterUser.Result.Success;
        var email1 = req1.email();
        var email2 = req2.email();
        var getRequest1 = new GetUserByEmailRequest(email1);
        var getRequest2 = new GetUserByEmailRequest(email2);
        var getResult1 = getUserByEmailHandler.handle(getRequest1);
        var getResult2 = getUserByEmailHandler.handle(getRequest2);
        assert getResult1 instanceof GetUserByEmailResults.Success;
        assert getResult2 instanceof GetUserByEmailResults.Success;
    }

    @Test
    void shouldGetUserByIdAfterRegisteringMultipleUsers() throws CheckedConstraintViolationException {
        var req1 = registerUserRequestMocker.createValidRequest();
        var req2 = registerUserRequestMocker.createValidRequest();
        var res1 = registerUserHandler.handle(req1);
        var res2 = registerUserHandler.handle(req2);
        assert res1 instanceof RegisterUser.Result.Success;
        assert res2 instanceof RegisterUser.Result.Success;
        var userId1 = ((RegisterUser.Result.Success) res1).userId();
        var userId2 = ((RegisterUser.Result.Success) res2).userId();
        var getRequest1 = new GetUserByIdRequest(userId1);
        var getRequest2 = new GetUserByIdRequest(userId2);
        var getResult1 = getUserByIdHandler.handle(getRequest1);
        var getResult2 = getUserByIdHandler.handle(getRequest2);
        assert getResult1 instanceof GetUserByIdResults.Success;
        assert getResult2 instanceof GetUserByIdResults.Success;
    }

}
