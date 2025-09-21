package tech.kood.match_me.connections;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ConnectionsConfig {

    @Bean
    @Qualifier("connectionsDataSource")
    @ConfigurationProperties("spring.datasource.connections")
    public DataSource connectionsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("connectionsNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate connectionsNamedParameterJdbcTemplate(
            @Qualifier("connectionsDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @Qualifier("connectionsJdbcTemplate")
    public JdbcTemplate connectionsJdbcTemplate(
            @Qualifier("connectionsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // @Qualifier("connectionsFlyway")
    // @Bean(initMethod = "migrate")
    // public Flyway connectionsFlyway(@Qualifier("connectionsDataSource") DataSource dataSource) {
    // var flyway = Flyway.configure().dataSource(dataSource)
    // .locations("classpath:/connections/database/flyway").load();
    // // flyway.migrate();
    // return flyway;
    // }
}
