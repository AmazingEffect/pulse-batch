package com.pulse.batch.config.postgre;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.pulse.batch.repository.social",
        entityManagerFactoryRef = "socialGraphEntityManagerFactory",
        transactionManagerRef = "socialGraphTransactionManager"
)
public class SocialGraphDataSourceConfig {

    @Bean(name = "socialGraphDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.social")
    public DataSource socialGraphDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "socialGraphEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean socialGraphEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("socialGraphDataSource") DataSource dataSource
    ) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "none");

        return builder
                .dataSource(dataSource)
                .packages("com.pulse.batch.entity.social")
                .persistenceUnit("socialGraph")
                .properties(properties)
                .build();
    }

    @Bean(name = "socialGraphTransactionManager")
    public PlatformTransactionManager socialGraphTransactionManager(
            @Qualifier("socialGraphEntityManagerFactory") EntityManagerFactory socialGraphEntityManagerFactory
    ) {
        return new JpaTransactionManager(socialGraphEntityManagerFactory);
    }


}