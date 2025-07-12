package tech.kood.match_me.user_management.feature;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserCommand;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
public class RegisterUserTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway") Flyway userManagementFlyway;

    @Autowired
    RegisterUserHandler registerUserHandler;

    @BeforeEach
    void setUp() {
        var result = userManagementFlyway.migrate();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateValidUser() {
        var request = RegisterUserRequestMocker.createValidRequest();
        var command = new RegisterUserCommand(request);
        registerUserHandler.handle(command);
        var result = command.getResultFuture().join();

        assert result instanceof RegisterUserResults.Success;
    }

    @Test
    void shouldReturnInvalidEmailForInvalidEmailRequest() {
        var request = RegisterUserRequestMocker.createInvalidEmailRequest();
        var command = new RegisterUserCommand(request);
        registerUserHandler.handle(command);
        var result = command.getResultFuture().join();

        assert result instanceof RegisterUserResults.InvalidEmail;
    }

    @Test
    void shouldThrowForBlankUsername() {
        try {
            var request = RegisterUserRequestMocker.createInvalidUsernameRequest();
            var command = new RegisterUserCommand(request);
            registerUserHandler.handle(command);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Username");
        }
    }

    @Test
    void shouldThrowForBlankPassword() {
        try {
            var request = RegisterUserRequestMocker.createInvalidPasswordRequest();
            var command = new RegisterUserCommand(request);
            registerUserHandler.handle(command);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Password");
        }
    }

    @Test
    void shouldThrowForNullFields() {
        try {
            var request = RegisterUserRequestMocker.createNullRequest();
            var command = new RegisterUserCommand(request);
            registerUserHandler.handle(command);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // Should mention one of the required fields
            assert e.getMessage().matches(".*(Username|Password|Email).*");
        }
    }

}