package tech.kood.match_me.user_management.feature;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import tech.kood.match_me.user_management.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RegisterUserTests {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.user-management.jdbc-url", postgres::getJdbcUrl);
        registry.add("spring.datasource.user-management.username", postgres::getUsername);
        registry.add("spring.datasource.user-management.password", postgres::getPassword);
        registry.add("spring.datasource.user-management.driver-class-name", postgres::getDriverClassName);
    }

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
    void OneIsOne() {
        assert 1 == 1;
    }

}