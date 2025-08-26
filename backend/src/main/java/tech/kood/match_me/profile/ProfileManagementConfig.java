package tech.kood.match_me.profile;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJpaRepositories(
    basePackages = "tech.kood.match_me.profile.repository",
    entityManagerFactoryRef = "profileManagementEmf",
    transactionManagerRef = "profileManagementTransactionManager"
)

@Configuration
public class ProfileManagementConfig {

    @Bean
    @Qualifier("profileManagementDataSource")
    @ConfigurationProperties("spring.datasource.profile")
    public DataSource profileDataManagementDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("profileJdbcTemplate")
    public JdbcTemplate profileManagementJdbcTemplate(
            @Qualifier("profileManagementDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Qualifier("profileManagementScheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(2); // Define 2 threads to run in parallel.
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean profileManagementEmf(
            EntityManagerFactoryBuilder builder,
            @Qualifier("profileManagementDataSource") DataSource ds) {

        return builder
                .dataSource(ds)
                .packages("tech.kood.match_me.profile.model")
                .persistenceUnit("profilePU")
                .build();
    }

    @Bean
    @Qualifier("profileManagementTransactionManager")
    public PlatformTransactionManager profileManagementTransactionManager(
            @Qualifier("profileManagementDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Qualifier("profileManagementTaskScheduler")
    public TaskScheduler taskScheduler(
            @Qualifier("profileManagementScheduledExecutorService") ScheduledExecutorService scheduledExecutorService) {
        return new ConcurrentTaskScheduler(scheduledExecutorService);
    }

    @Bean
    @Qualifier("profileManagementNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate profileManagementNamedParameterJdbcTemplate(
            @Qualifier("profileManagementDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
    
}
