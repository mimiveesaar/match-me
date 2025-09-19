package tech.kood.match_me.matching;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@EnableJpaRepositories(basePackages = "tech.kood.match_me.matching.repository",
        entityManagerFactoryRef = "matchingEmf",
        transactionManagerRef = "matchingTransactionManager")
@Configuration
public class MatchingConfig {

    @Bean
    @Qualifier("matchingDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.matching")
    public DataSource matchingDataSource() {
        return (DataSource) DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("matchingJdbcTemplate")
    public JdbcTemplate matchingJdbcTemplate(
            @Qualifier("matchingDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("matchingScheduledExecutorService")
    public ScheduledExecutorService matchingScheduledExecutorService() {
        return Executors.newScheduledThreadPool(2);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.match.jpa")
    public Properties matchingJpaProperties() {
        return new Properties();
    }

    @Bean
    @Qualifier("matchingEmf")
    public LocalContainerEntityManagerFactoryBean matchingEmf(
            @Qualifier("matchingDataSource") DataSource dataSource,
            @Qualifier("matchingJpaProperties") Properties jpaProps
    ) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("tech.kood.match_me.matching.model");
        emf.setPersistenceUnitName("matchingPU");
        emf.setJpaVendorAdapter(vendorAdapter);

        emf.setJpaProperties(jpaProps);
        return emf;
    }

    @Bean(name = "matchingTransactionManager")
    @Primary
    public PlatformTransactionManager matchingTransactionManager(
            @Qualifier("matchingEmf") LocalContainerEntityManagerFactoryBean matchingEmf) {
        return new JpaTransactionManager(
                java.util.Objects.requireNonNull(matchingEmf.getObject(),
                        "EntityManagerFactory must not be null"));
    }

    @Bean("matchingTaskScheduler")
    public TaskScheduler matchingTaskScheduler(
            @Qualifier("matchingScheduledExecutorService") ScheduledExecutorService scheduledExecutorService) {
        return new ConcurrentTaskScheduler(scheduledExecutorService);
    }

    @Bean
    @Qualifier("matchingNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate matchingNamedParameterJdbcTemplate(
            @Qualifier("matchingDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}