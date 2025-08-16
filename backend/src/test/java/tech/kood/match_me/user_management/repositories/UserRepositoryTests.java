package tech.kood.match_me.user_management.repositories;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.features.user.persistance.UserRepository;
import tech.kood.match_me.user_management.internal.features.user.features.registerUser.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.mocks.UserEntityMother;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
public class UserRepositoryTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway")
    Flyway userManagementFlyway;

    @Autowired
    UserEntityMother userEntityMocker;

    @Autowired
    RegisterUserCommandHandler registerUserHandler;


    @Test
    void shouldCreateValidUser() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
    }

    @Test
    void shouldFindUserByUsername() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserByUsername(userEntity.getUsername());
        assertFalse(found.isEmpty(), "User should be found by username");
        assertEquals(userEntity.getUsername(), found.get().getUsername());
    }

    @Test
    void shouldFindUserByEmail() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserByEmail(userEntity.getEmail());
        assertFalse(found.isEmpty(), "User should be found by email");
        assertEquals(userEntity.getEmail(), found.get().getEmail());
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
        var found = userRepository.findUserById(userEntity.getId());
        assertFalse(found.isEmpty(), "User should be found by ID");
        assertEquals(userEntity.getId(), found.get().getId());
    }

    @Test
    void shouldReturnTrueIfUsernameExists() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        assertTrue(userRepository.usernameExists(userEntity.getUsername()));
    }

    @Test
    void shouldReturnTrueIfEmailExists() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        assertTrue(userRepository.emailExists(userEntity.getEmail()));
    }

    @Test
    void shouldDeleteAllUsers() {
        var userEntity = userEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        userRepository.deleteAll();
        assertFalse(userRepository.usernameExists(userEntity.getUsername()));
        assertFalse(userRepository.emailExists(userEntity.getEmail()));
    }
}
