package tech.kood.match_me.user_management.common;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;


@SpringBootTest(classes = tech.kood.match_me.user_management.UserManagementTestApplication.class)
// @ComponentScan(basePackages = "tech.kood.match_me.user_management",
// excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
// pattern = "tech.kood.match_me.(?!user_management).*"))
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class UserManagementTestBase {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine").withReuse(true);

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.user-management.jdbc-url", postgres::getJdbcUrl);
        registry.add("spring.datasource.user-management.username", postgres::getUsername);
        registry.add("spring.datasource.user-management.password", postgres::getPassword);
        registry.add("spring.datasource.user-management.driver-class-name",
                postgres::getDriverClassName);
    }
}
