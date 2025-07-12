package tech.kood.match_me.user_management.feature;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.getUser.GetUserHandler;
import tech.kood.match_me.user_management.internal.features.getUser.commands.GetUserByEmailCommand;
import tech.kood.match_me.user_management.internal.features.getUser.commands.GetUserByIdCommand;
import tech.kood.match_me.user_management.internal.features.getUser.commands.GetUserByUsernameCommand;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserCommand;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class GetUserTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway") Flyway userManagementFlyway;

    @Autowired
    RegisterUserHandler registerUserHandler;

    @Autowired
    GetUserHandler getUserHandler;

    @BeforeEach
    void setUp() {
        userManagementFlyway.migrate();
        userRepository.deleteAll();
    }

    @Test
    void shouldGetUserByUsername() {
        var registerRequest = RegisterUserRequestMocker.createValidRequest();
        var registerCommand = new RegisterUserCommand(registerRequest);
        registerUserHandler.handle(registerCommand);
        var registered = registerCommand.getResultFuture().join();
        assert registered instanceof tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults.Success;

        var getRequest = new GetUserByUsernameRequest(UUID.randomUUID(), registerRequest.username(), registerRequest.tracingId());
        var getCommand = new GetUserByUsernameCommand(getRequest);
        getUserHandler.handle(getCommand);
        var result = getCommand.getResultFuture().join();
        assert result instanceof GetUserByUsernameResults.Success;
    }

    @Test
    void shouldReturnUserNotFoundByUsername() {
        var getRequest = new GetUserByUsernameRequest(UUID.randomUUID(), "nonexistent", Optional.of("trace"));
        var getCommand = new GetUserByUsernameCommand(getRequest);
        getUserHandler.handle(getCommand);
        var result = getCommand.getResultFuture().join();
        assert result instanceof GetUserByUsernameResults.UserNotFound;
    }

    @Test
    void shouldGetUserByEmail() {
        var registerRequest = RegisterUserRequestMocker.createValidRequest();
        var registerCommand = new RegisterUserCommand(registerRequest);
        registerUserHandler.handle(registerCommand);
        var registered = registerCommand.getResultFuture().join();
        assert registered instanceof tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults.Success;

        var getRequest = new GetUserByEmailRequest(UUID.randomUUID(), registerRequest.email(), registerRequest.tracingId());
        var getCommand = new GetUserByEmailCommand(getRequest);
        getUserHandler.handle(getCommand);
        var result = getCommand.getResultFuture().join();
        assert result instanceof GetUserByEmailResults.Success;
    }

    @Test
    void shouldReturnUserNotFoundByEmail() {
        var getRequest = new GetUserByEmailRequest(UUID.randomUUID(), "notfound@email.com", Optional.of("trace"));
        var getCommand = new GetUserByEmailCommand(getRequest);
        getUserHandler.handle(getCommand);
        var result = getCommand.getResultFuture().join();
        assert result instanceof GetUserByEmailResults.UserNotFound;
    }

    @Test
    void shouldGetUserById() {
        var registerRequest = RegisterUserRequestMocker.createValidRequest();
        var registerCommand = new RegisterUserCommand(registerRequest);
        registerUserHandler.handle(registerCommand);
        var registered = registerCommand.getResultFuture().join();
        assert registered instanceof tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults.Success;
        var user = ((tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults.Success) registered).user();

        var getRequest = new GetUserByIdRequest(UUID.randomUUID(), user.id(), registerRequest.tracingId());
        var getCommand = new GetUserByIdCommand(getRequest);
        getUserHandler.handle(getCommand);
        var result = getCommand.getResultFuture().join();
        assert result instanceof GetUserByIdResults.Success;
    }

    @Test
    void shouldReturnUserNotFoundById() {
        var getRequest = new GetUserByIdRequest(UUID.randomUUID(), UUID.randomUUID(), Optional.of("trace"));
        var getCommand = new GetUserByIdCommand(getRequest);
        getUserHandler.handle(getCommand);
        var result = getCommand.getResultFuture().join();
        assert result instanceof GetUserByIdResults.UserNotFound;
    }
}
