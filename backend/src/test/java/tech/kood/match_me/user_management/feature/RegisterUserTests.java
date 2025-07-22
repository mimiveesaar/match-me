package tech.kood.match_me.user_management.feature;

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
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) 
@Transactional
public class RegisterUserTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway") Flyway userManagementFlyway;

    @Autowired
    RegisterUserHandler registerUserHandler;


    @BeforeAll
    void migrate() {
        var result = userManagementFlyway.migrate();
    }

    @Test
    void shouldCreateValidUser() {
        var request = RegisterUserRequestMocker.createValidRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.Success;
    }

    @Test
    void shouldNotCreateUserWithExistingUsername() {
        var request = RegisterUserRequestMocker.createValidRequest();
        registerUserHandler.handle(request);
        var result = registerUserHandler.handle(request);
        var result2 = registerUserHandler.handle(request);
        assert result2 instanceof RegisterUserResults.UsernameExists;
    }

    @Test
    void shouldNotCreateUserWithExistingEmail() {
        var request = RegisterUserRequestMocker.createValidRequest();
        var request2 = RegisterUserRequestMocker.createValidRequest();
        request2 = request2.withEmail(request.email());
        registerUserHandler.handle(request);
        var result = registerUserHandler.handle(request2);
        assert result instanceof RegisterUserResults.EmailExists;
    }

    @Test
    void shouldNotCreateUserWithInvalidEmail() {
        var request = RegisterUserRequestMocker.createInvalidEmailRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidEmail;
    }

    @Test
    void shouldNotCreateUserWithShortUsername() {
        var request = RegisterUserRequestMocker.createShortUsernameRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidUsername;
        assert ((RegisterUserResults.InvalidUsername) result).type() == RegisterUserResults.InvalidUsernameType.TOO_SHORT;
    }

    @Test
    void shouldNotCreateUserWithLongUsername() {
        var request = RegisterUserRequestMocker.createLongUsernameRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidUsername;
        assert ((RegisterUserResults.InvalidUsername) result).type() == RegisterUserResults.InvalidUsernameType.TOO_LONG;
    }

    @Test
    void shouldNotCreateUserWithInvalidUsername() {
        var request = RegisterUserRequestMocker.createInvalidUsernameRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidUsername;
        assert ((RegisterUserResults.InvalidUsername) result).type() == RegisterUserResults.InvalidUsernameType.INVALID_CHARACTERS;
    }

    @Test
    void shouldNotCreateUserWithInvalidPassword() {
        var request = RegisterUserRequestMocker.createInvalidPasswordRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidPassword;
        assert ((RegisterUserResults.InvalidPassword) result).type() == RegisterUserResults.InvalidPasswordType.TOO_SHORT;
    }

    @Test
    void shouldNotCreateUserWithNullRequest() {
        var request = RegisterUserRequestMocker.createNullRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidUsername;
        assert ((RegisterUserResults.InvalidUsername) result).type() == RegisterUserResults.InvalidUsernameType.TOO_SHORT;
    }

    @Test
    void shouldNotCreateUserWithEmptyUsername() {
            var request = RegisterUserRequestMocker.createEmptyUsernameRequest();
            var result = registerUserHandler.handle(request);
            assert result instanceof RegisterUserResults.InvalidUsername;
            assert ((RegisterUserResults.InvalidUsername) result).type() == RegisterUserResults.InvalidUsernameType.TOO_SHORT;
    }
    @Test
    void shouldNotCreateUserWithEmptyPassword() {
        var request = RegisterUserRequestMocker.createEmptyPasswordRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidPassword;
        assert ((RegisterUserResults.InvalidPassword) result).type() == RegisterUserResults.InvalidPasswordType.TOO_SHORT;
    }

    @Test
    void shouldNotCreateUserWithEmptyEmail() {
        var request = RegisterUserRequestMocker.createEmptyEmailRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidEmail;
    }

    @Test
    void shouldNotCreateUserWithLongPassword() {
        var request = RegisterUserRequestMocker.createLongPasswordRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidPassword;
        assert ((RegisterUserResults.InvalidPassword) result).type() == RegisterUserResults.InvalidPasswordType.TOO_LONG;
    }
}