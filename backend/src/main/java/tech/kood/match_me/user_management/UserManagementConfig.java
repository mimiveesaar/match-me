package tech.kood.match_me.user_management;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class UserManagementConfig {
    
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
    @Qualifier("userManagementNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate userManagementNamedParameterJdbcTemplate(
        @Qualifier("userManagementDataSource") DataSource dataSource
    ) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

     @Bean(initMethod = "migrate")
    public Flyway userManagementFlyway(@Qualifier("userManagementDataSource") DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/user_management/database/flyway")
                .baselineOnMigrate(true)
                .load();
    }
}
