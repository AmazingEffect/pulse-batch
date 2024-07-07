package com.pulse.batch.config.batch;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class BatchConfigurer extends DefaultBatchConfiguration {

    private final DataSource memberDataSource;
    private final PlatformTransactionManager memberTransactionManager;

    public BatchConfigurer(@Qualifier("memberDataSource") DataSource memberDataSource,
                           @Qualifier("memberTransactionManager") PlatformTransactionManager memberTransactionManager) {
        this.memberDataSource = memberDataSource;
        this.memberTransactionManager = memberTransactionManager;
    }

    @Override
    @NonNull
    protected DataSource getDataSource() {
        return memberDataSource;
    }

    @Override
    @Bean
    @NonNull
    public JobRepository jobRepository() {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(memberDataSource);
        factory.setTransactionManager(memberTransactionManager);
        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        factory.setTablePrefix("BATCH_");
        try {
            factory.afterPropertiesSet();
            return Objects.requireNonNull(factory.getObject());
        } catch (Exception e) {
            throw new RuntimeException("Could not create JobRepository", e);
        }
    }

    @Bean
    @NonNull
    public PlatformTransactionManager transactionManager() {
        return memberTransactionManager;
    }

}
