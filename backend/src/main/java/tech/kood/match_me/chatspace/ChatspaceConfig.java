package tech.kood.match_me.chatspace;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJpaRepositories(basePackages = "tech.kood.match_me.chatspace.repository",
        entityManagerFactoryRef = "chatspaceEmf",
        transactionManagerRef = "chatspaceTransactionManager")
@Configuration
public class ChatspaceConfig {

    @Bean
    @Qualifier("chatspaceDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.chatspace")
    public DataSource chatspaceDataSource() {
        return (DataSource) DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("chatspaceJdbcTemplate")
    public JdbcTemplate chatspaceJdbcTemplate(
            @Qualifier("chatspaceDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("chatspaceScheduledExecutorService")
    public ScheduledExecutorService chatspaceScheduledExecutorService() {
        return Executors.newScheduledThreadPool(2);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.chatspace.jpa")
    public Properties chatspaceJpaProperties() {
        return new Properties();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean chatspaceEmf(
            @Qualifier("chatspaceDataSource") DataSource dataSource,
            @Qualifier("chatspaceJpaProperties") Properties jpaProps) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("tech.kood.match_me.chatspace.model");
        emf.setPersistenceUnitName("chatspacePU");
        emf.setJpaVendorAdapter(vendorAdapter);

        emf.setJpaProperties(jpaProps);
        return emf;
    }

    @Bean(name = "chatspaceTransactionManager")
    public PlatformTransactionManager chatspaceTransactionManager(
            @Qualifier("chatspaceEmf") LocalContainerEntityManagerFactoryBean chatspaceEmf) {
        return new org.springframework.orm.jpa.JpaTransactionManager(
                java.util.Objects.requireNonNull(chatspaceEmf.getObject(),
                        "EntityManagerFactory must not be null"));
    }

    @Bean("chatspaceTaskScheduler")
    public TaskScheduler chatspaceTaskScheduler(
            @Qualifier("chatspaceScheduledExecutorService") ScheduledExecutorService scheduledExecutorService) {
        return new ConcurrentTaskScheduler(scheduledExecutorService);
    }

    @Bean
    @Qualifier("chatspaceNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate chatspaceNamedParameterJdbcTemplate(
            @Qualifier("chatspaceDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}