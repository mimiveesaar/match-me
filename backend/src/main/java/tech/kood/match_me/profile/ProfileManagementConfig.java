package tech.kood.match_me.profile;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJpaRepositories(basePackages = "tech.kood.match_me.profile.repository",
        entityManagerFactoryRef = "profileManagementEmf",
        transactionManagerRef = "profileManagementTransactionManager")
@Configuration
public class ProfileManagementConfig {

    @Bean
    @Qualifier("profileManagementDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.profile")
    public DataSource profileManagementDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("profileJdbcTemplate")
    public JdbcTemplate profileManagementJdbcTemplate(
            @Qualifier("profileManagementDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("profileManagementScheduledExecutorService")
    public ScheduledExecutorService profileManagementScheduledExecutorService() {
        return Executors.newScheduledThreadPool(2);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean profileManagementEmf(
            @Qualifier("profileManagementDataSource") DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("tech.kood.match_me.profile.model");
        emf.setPersistenceUnitName("profilePU");
        emf.setJpaVendorAdapter(vendorAdapter);

        return emf;
    }

    @Bean
    @Qualifier("profileManagementTransactionManager")
    public PlatformTransactionManager profileManagementTransactionManager(
            @Qualifier("profileManagementDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("profileManagementTaskScheduler")
    public TaskScheduler profileManagementTaskScheduler(
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
