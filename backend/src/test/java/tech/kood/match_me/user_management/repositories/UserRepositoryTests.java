package tech.kood.match_me.user_management.repositories;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.mocks.UserEntityMocker;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTests extends UserManagementTestBase {
    
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
        var userEntity = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
    }

    @Test
    void shouldFindUserByUsername() {
        var userEntity = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserByUsername(userEntity.username());
        assertFalse(found.isEmpty(), "User should be found by username");
        assertEquals(userEntity.username(), found.get().username());
    }

    @Test
    void shouldFindUserByEmail() {
        var userEntity = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserByEmail(userEntity.email());
        assertFalse(found.isEmpty(), "User should be found by email");
        assertEquals(userEntity.email(), found.get().email());
    }

    @Test
    void shouldFindUserById() {
        var userEntity = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserById(userEntity.id());
        assertFalse(found.isEmpty(), "User should be found by ID");
        assertEquals(userEntity.id(), found.get().id());
    }

    @Test
    void shouldReturnTrueIfUsernameExists() {
        var userEntity = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        assertTrue(userRepository.usernameExists(userEntity.username()));
    }

    @Test
    void shouldReturnTrueIfEmailExists() {
        var userEntity = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        assertTrue(userRepository.emailExists(userEntity.email()));
    }

    @Test
    void shouldDeleteAllUsers() {
        var userEntity = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        userRepository.deleteAll();
        assertFalse(userRepository.usernameExists(userEntity.username()));
        assertFalse(userRepository.emailExists(userEntity.email()));
    }
}