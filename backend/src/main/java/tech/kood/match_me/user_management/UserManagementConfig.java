package tech.kood.match_me.user_management;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Configuration
public class UserManagementConfig {
    

    @Value("${user-management.username.min-length:3}")
    private int usernameMinLength;
    @Value("${user-management.username.max-length:20}")
    private int usernameMaxLength;
    @Value("${user-management.password.min-length:8}")
    private int passwordMinLength;
    @Value("${user-management.password.max-length:64}")
    private int passwordMaxLength;

    @Value("${user-management.jwt.secret}")
    private String jwtSecret;

    @Value("${user-management.jwt.expiration:3600}")
    private int jwtExpiration;

    @Value("${user-management.refresh-token.expiration}")
    private int refreshTokenExpiration;

    @Value("${user-management.refresh-token.cleanup-interval}")
    private int refreshTokenCleanupInterval;

    public int getRefreshTokenCleanupInterval() {
        return refreshTokenCleanupInterval;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public int getJwtExpiration() {
        return jwtExpiration;
    }

    public int getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public int getUsernameMinLength() {
        return usernameMinLength;
    }

    public int getUsernameMaxLength() {
        return usernameMaxLength;
    }

    public int getPasswordMinLength() {
        return passwordMinLength;
    }

    public int getPasswordMaxLength() {
        return passwordMaxLength;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.user-management")
    public DataSource userManagementDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("userManagementJdbcTemplate")
    public JdbcTemplate userManagementJdbcTemplate(
        @Qualifier("userManagementDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Qualifier("userManagementScheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(2); // Define 2 threads to run in parallel.
    }

    @Bean
    @Qualifier("userManagementTaskScheduler")
    public TaskScheduler taskScheduler(@Qualifier("userManagementScheduledExecutorService") ScheduledExecutorService scheduledExecutorService) {
        return new ConcurrentTaskScheduler(scheduledExecutorService); 
    }

    @Bean
    @Qualifier("userManagementNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate userManagementNamedParameterJdbcTemplate(
        @Qualifier("userManagementDataSource") DataSource dataSource
    ) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public Flyway userManagementFlyway(@Qualifier("userManagementDataSource") DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/user_management/database/flyway")
                .baselineOnMigrate(true)
                .load();
    }
}
