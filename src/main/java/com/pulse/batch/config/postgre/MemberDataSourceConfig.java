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
        basePackages = "com.pulse.batch.repository.member",
        entityManagerFactoryRef = "memberEntityManagerFactory",
        transactionManagerRef = "memberTransactionManager"
)
public class MemberDataSourceConfig {

    @Bean(name = "memberDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.member")
    public DataSource memberDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "memberEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean memberEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("memberDataSource") DataSource dataSource
    ) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("hibernate.hbm2ddl.auto", "none");

        return builder
                .dataSource(dataSource)
                .packages("com.pulse.batch.entity.member")
                .persistenceUnit("member")
                .properties(properties)
                .build();
    }

    @Bean(name = "memberTransactionManager")
    public PlatformTransactionManager memberTransactionManager(
            @Qualifier("memberEntityManagerFactory") EntityManagerFactory memberEntityManagerFactory
    ) {
        return new JpaTransactionManager(memberEntityManagerFactory);
    }

}

