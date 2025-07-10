package tech.kood.match_me.user_management.feature;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.features.registerUser.RegisterUserResults;
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
        var result = registerUserHandler.handle(RegisterUserRequestMocker.createValidRequest());
        assert result instanceof RegisterUserResults.Success;
    }

    @Test
    void shouldReturnInvalidEmailForInvalidEmailRequest() {
        var result = registerUserHandler.handle(RegisterUserRequestMocker.createInvalidEmailRequest());
        assert result instanceof RegisterUserResults.InvalidEmail;
    }

    @Test
    void shouldThrowForBlankUsername() {
        try {
            registerUserHandler.handle(RegisterUserRequestMocker.createInvalidUsernameRequest());
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Username");
        }
    }

    @Test
    void shouldThrowForBlankPassword() {
        try {
            registerUserHandler.handle(RegisterUserRequestMocker.createInvalidPasswordRequest());
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Password");
        }
    }

    @Test
    void shouldThrowForNullFields() {
        try {
            registerUserHandler.handle(RegisterUserRequestMocker.createNullRequest());
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // Should mention one of the required fields
            assert e.getMessage().matches(".*(Username|Password|Email).*");
        }
    }

}