package tech.kood.match_me.user_management.repositories;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.mocks.UserEntityMocker;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
public class UserRepositoryTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway")
    Flyway userManagementFlyway;

    @Autowired
    UserEntityMocker userEntityMocker;

    @Autowired
    RegisterUserHandler registerUserHandler;


    @Test
    void shouldCreateValidUser() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
    }

    @Test
    void shouldFindUserByUsername() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserByUsername(userEntity.username());
        assertFalse(found.isEmpty(), "User should be found by username");
        assertEquals(userEntity.username(), found.get().username());
    }

    @Test
    void shouldFindUserByEmail() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserByEmail(userEntity.email());
        assertFalse(found.isEmpty(), "User should be found by email");
        assertEquals(userEntity.email(), found.get().email());
    }


    @Test
    void shouldNotFindUserByEmail() {
        var found = userRepository.findUserByEmail("test@mail.com");
        assertTrue(found.isEmpty(), "User should be found by email");
    }

    @Test
    void emailShouldNotExist() {
        var exists = userRepository.emailExists("test@mail.com");
        assertFalse(exists, "Email should not exist");
    }

    @Test
    void shouldFindUserById() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserById(userEntity.id());
        assertFalse(found.isEmpty(), "User should be found by ID");
        assertEquals(userEntity.id(), found.get().id());
    }

    @Test
    void shouldReturnTrueIfUsernameExists() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        assertTrue(userRepository.usernameExists(userEntity.username()));
    }

    @Test
    void shouldReturnTrueIfEmailExists() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        assertTrue(userRepository.emailExists(userEntity.email()));
    }

    @Test
    void shouldDeleteAllUsers() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        userRepository.deleteAll();
        assertFalse(userRepository.usernameExists(userEntity.username()));
        assertFalse(userRepository.emailExists(userEntity.email()));
    }
}
