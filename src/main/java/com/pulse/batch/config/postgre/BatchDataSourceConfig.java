package com.pulse.batch.config.postgre;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        basePackages = "com.pulse.batch.repository.batch",
        entityManagerFactoryRef = "batchEntityManagerFactory",
        transactionManagerRef = "batchTransactionManager"
)
public class BatchDataSourceConfig {

    @Primary
    @Bean(name = "batchDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Primary
    @Bean(name = "batchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("batchDataSource") DataSource dataSource
    ) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");

        return builder
                .dataSource(dataSource)
                .packages("com.pulse.batch.entity.batch")
                .persistenceUnit("batch")
                .properties(properties)
                .build();
    }


    @Primary
    @Bean(name = "batchTransactionManager")
    public PlatformTransactionManager batchTransactionManager(
            @Qualifier("batchEntityManagerFactory") EntityManagerFactory batchEntityManagerFactory
    ) {
        return new JpaTransactionManager(batchEntityManagerFactory);
    }

}
