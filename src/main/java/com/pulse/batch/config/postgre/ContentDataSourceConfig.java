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
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.pulse.batch.repository.content",
        entityManagerFactoryRef = "contentEntityManagerFactory",
        transactionManagerRef = "contentTransactionManager"
)
public class ContentDataSourceConfig {

    @Bean(name = "contentDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.content")
    public DataSource contentDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "contentEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean contentEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("contentDataSource") DataSource dataSource
    ) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "create");

        return builder
                .dataSource(dataSource)
                .packages("com.pulse.batch.entity.content")
                .persistenceUnit("content")
                .properties(properties)
                .build();
    }

    @Bean(name = "contentTransactionManager")
    public PlatformTransactionManager contentTransactionManager(
            @Qualifier("contentEntityManagerFactory") EntityManagerFactory contentEntityManagerFactory
    ) {
        return new JpaTransactionManager(contentEntityManagerFactory);
    }

}