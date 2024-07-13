package com.pulse.batch.config;

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

/**
 * Batch 설정을 위한 클래스
 * 여기서 Batch의 기본 6개 테이블이 설정된 batch 데이터소스와 트랜잭션 매니저를 설정한다.
 */
@Configuration
public class BatchDefaultConfig extends DefaultBatchConfiguration {

    private final DataSource batchDataSource;
    private final PlatformTransactionManager batchTransactionManager;

    public BatchDefaultConfig(@Qualifier("batchDataSource") DataSource batchDataSource,
                              @Qualifier("batchTransactionManager") PlatformTransactionManager batchTransactionManager) {
        this.batchDataSource = batchDataSource;
        this.batchTransactionManager = batchTransactionManager;
    }

    @Override
    @NonNull
    protected DataSource getDataSource() {
        return batchDataSource;
    }

    @Override
    @Bean
    @NonNull
    public JobRepository jobRepository() {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(batchDataSource);
        factory.setTransactionManager(batchTransactionManager);
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
        return batchTransactionManager;
    }

}
