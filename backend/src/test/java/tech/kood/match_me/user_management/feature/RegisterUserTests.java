package tech.kood.match_me.user_management.feature;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class RegisterUserTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway")
    Flyway userManagementFlyway;

    @Autowired
    RegisterUserCommandHandler registerUserHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Test
    void shouldCreateValidUser() {
        var request = registerUserRequestMocker.createValidRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.Success;
    }

    @Test
    void shouldNotCreateUserWithExistingUsername() {
        var request = registerUserRequestMocker.createValidRequest();
        registerUserHandler.handle(request);
        var result = registerUserHandler.handle(request);
        var result2 = registerUserHandler.handle(request);
        assert result2 instanceof RegisterUserResults.UsernameExists;
    }

    @Test
    void shouldNotCreateUserWithExistingEmail() {
        var request = registerUserRequestMocker.createValidRequest();
        var request2 = registerUserRequestMocker.createValidRequest();
        request2 = request2.withEmail(request.email());
        registerUserHandler.handle(request);
        var result = registerUserHandler.handle(request2);
        assert result instanceof RegisterUserResults.EmailExists;
    }

    @Test
    void shouldNotCreateUserWithShortUsername() {
        var request = registerUserRequestMocker.createShortUsernameRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidUsername;
        assert ((RegisterUserResults.InvalidUsername) result)
                .getType() == RegisterUserResults.InvalidUsernameType.TOO_SHORT;
    }

    @Test
    void shouldNotCreateUserWithLongUsername() {
        var request = registerUserRequestMocker.createLongUsernameRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidUsername;
        assert ((RegisterUserResults.InvalidUsername) result)
                .getType() == RegisterUserResults.InvalidUsernameType.TOO_LONG;
    }

    @Test
    void shouldNotCreateUserWithInvalidUsername() {
        var request = registerUserRequestMocker.createInvalidUsernameRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidUsername;
        assert ((RegisterUserResults.InvalidUsername) result)
                .getType() == RegisterUserResults.InvalidUsernameType.INVALID_CHARACTERS;
    }

    @Test
    void shouldNotCreateUserWithInvalidPassword() {
        var request = registerUserRequestMocker.createInvalidPasswordRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidPassword;
        assert ((RegisterUserResults.InvalidPassword) result)
                .getType() == RegisterUserResults.InvalidPasswordType.TOO_SHORT;
    }

    @Test
    void shouldNotCreateUserWithNullRequest() {
        var request = registerUserRequestMocker.createNullRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidUsername;
        assert ((RegisterUserResults.InvalidUsername) result)
                .getType() == RegisterUserResults.InvalidUsernameType.TOO_SHORT;
    }

    @Test
    void shouldNotCreateUserWithEmptyUsername() {
        var request = registerUserRequestMocker.createEmptyUsernameRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidUsername;
        assert ((RegisterUserResults.InvalidUsername) result)
                .getType() == RegisterUserResults.InvalidUsernameType.TOO_SHORT;
    }

    @Test
    void shouldNotCreateUserWithEmptyPassword() {
        var request = registerUserRequestMocker.createEmptyPasswordRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidPassword;
        assert ((RegisterUserResults.InvalidPassword) result)
                .getType() == RegisterUserResults.InvalidPasswordType.TOO_SHORT;
    }

    @Test
    void shouldNotCreateUserWithLongPassword() {
        var request = registerUserRequestMocker.createLongPasswordRequest();
        var result = registerUserHandler.handle(request);
        assert result instanceof RegisterUserResults.InvalidPassword;
        assert ((RegisterUserResults.InvalidPassword) result)
                .getType() == RegisterUserResults.InvalidPasswordType.TOO_LONG;
    }
}
