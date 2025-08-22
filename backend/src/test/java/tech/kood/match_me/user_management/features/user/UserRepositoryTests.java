package tech.kood.match_me.user_management.features.user;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
public class UserRepositoryTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway")
    Flyway userManagementFlyway;

    @Autowired
    UserEntityMother userEntityMother;

    @Test
    void shouldCreateValidUser() throws CheckedConstraintViolationException {
        var userEntity = userEntityMother.createValidUserEntity();
        userRepository.saveUser(userEntity);
    }

    @Test
    void shouldFindUserByEmail() throws CheckedConstraintViolationException {
        var userEntity = userEntityMother.createValidUserEntity();
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
    void shouldFindUserById() throws CheckedConstraintViolationException {
        var userEntity = userEntityMother.createValidUserEntity();
        userRepository.saveUser(userEntity);
        var found = userRepository.findUserById(userEntity.getId());
        assertFalse(found.isEmpty(), "User should be found by ID");
        assertEquals(userEntity.getId(), found.get().getId());
    }

    @Test
    void shouldReturnTrueIfEmailExists() throws CheckedConstraintViolationException {
        var userEntity = userEntityMother.createValidUserEntity();
        userRepository.saveUser(userEntity);
        assertTrue(userRepository.emailExists(userEntity.getEmail()));
    }

    @Test
    void shouldDeleteAllUsers() throws CheckedConstraintViolationException {
        var userEntity = userEntityMother.createValidUserEntity();
        userRepository.saveUser(userEntity);
        userRepository.deleteAll();
        assertFalse(userRepository.emailExists(userEntity.getEmail()));
    }
}
