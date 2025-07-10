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
}