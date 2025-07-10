package tech.kood.match_me.user_management.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
public abstract class UserManagementTestBase {
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

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
}