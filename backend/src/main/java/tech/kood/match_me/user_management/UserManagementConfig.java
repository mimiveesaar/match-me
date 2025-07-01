package tech.kood.match_me.user_management;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class UserManagementConfig {
    
    @Bean
    @ConfigurationProperties("spring.datasource.user-management")
    public DataSource userManagementDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate userManagementJdbcTemplate(
        @Qualifier("userManagementDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }
}
