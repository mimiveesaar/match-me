package tech.kood.match_me.connections.common;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(classes = tech.kood.match_me.connections.ConnectionsTestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ConnectionsTestBase {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine").withReuse(true);

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.connections.jdbc-url", postgres::getJdbcUrl);
        registry.add("spring.datasource.connections.username", postgres::getUsername);
        registry.add("spring.datasource.connections.password", postgres::getPassword);
        registry.add("spring.datasource.connections.driver-class-name",
                postgres::getDriverClassName);
    }
}
