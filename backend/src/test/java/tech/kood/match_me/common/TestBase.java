package tech.kood.match_me.common;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;


@SpringBootTest()
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestBase {

    @Container
    protected static final PostgreSQLContainer<?> userManagementPostgres =
            new PostgreSQLContainer<>("postgres:16-alpine").withReuse(false);

    @Container
    protected static final PostgreSQLContainer<?> connectionsPostgres =
            new PostgreSQLContainer<>("postgres:16-alpine").withReuse(false);

    @Container
    protected static final PostgreSQLContainer<?> chatspacePostgres =
            new PostgreSQLContainer<>("postgres:16-alpine").withReuse(false);

    @Container
    protected static final PostgreSQLContainer<?> matchingPostgres =
            new PostgreSQLContainer<>("postgres:16-alpine").withReuse(false);

    @Container
    protected static final PostgreSQLContainer<?> profilePostgres =
            new PostgreSQLContainer<>("postgres:16-alpine").withReuse(false);

    static {
        userManagementPostgres.start();
        connectionsPostgres.start();
        chatspacePostgres.start();
        matchingPostgres.start();
        profilePostgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.user-management.jdbc-url", userManagementPostgres::getJdbcUrl);
        registry.add("spring.datasource.user-management.username", userManagementPostgres::getUsername);
        registry.add("spring.datasource.user-management.password", userManagementPostgres::getPassword);
        registry.add("spring.datasource.user-management.driver-class-name",
                userManagementPostgres::getDriverClassName);

        registry.add("spring.datasource.connections.jdbc-url", connectionsPostgres::getJdbcUrl);
        registry.add("spring.datasource.connections.username", connectionsPostgres::getUsername);
        registry.add("spring.datasource.connections.password", connectionsPostgres::getPassword);
        registry.add("spring.datasource.connections.driver-class-name",
                connectionsPostgres::getDriverClassName);

        registry.add("spring.datasource.chatspace.jdbc-url", chatspacePostgres::getJdbcUrl);
        registry.add("spring.datasource.chatspace.username", chatspacePostgres::getUsername);
        registry.add("spring.datasource.chatspace.password", chatspacePostgres::getPassword);
        registry.add("spring.datasource.chatspace.driver-class-name",
                chatspacePostgres::getDriverClassName);

        registry.add("spring.datasource.matching.jdbc-url", matchingPostgres::getJdbcUrl);
        registry.add("spring.datasource.matching.username", matchingPostgres::getUsername);
        registry.add("spring.datasource.matching.password", matchingPostgres::getPassword);
        registry.add("spring.datasource.matching.driver-class-name",
                matchingPostgres::getDriverClassName);

        registry.add("spring.datasource.profile.jdbc-url", profilePostgres::getJdbcUrl);
        registry.add("spring.datasource.profile.username", profilePostgres::getUsername);
        registry.add("spring.datasource.profile.password", profilePostgres::getPassword);
        registry.add("spring.datasource.profile.driver-class-name",
                profilePostgres::getDriverClassName);
    }
}
