package tech.kood.match_me.user_management.repositories;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.features.registerUser.RegisterUserHandler;
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
        assertNotNull(found);
        assertEquals(userEntity.username(), found.username());
    }

    @Test
    void shouldFindUserByEmail() {
        var userEntity = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserByEmail(userEntity.email());
        assertNotNull(found);
        assertEquals(userEntity.email(), found.email());
    }

    @Test
    void shouldFindUserById() {
        var userEntity = UserEntityMocker.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserById(userEntity.id());
        assertNotNull(found);
        assertEquals(userEntity.id(), found.id());
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