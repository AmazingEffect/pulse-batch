package com.pulse.batch.config;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * Batch Job에 대한 설정을 위한 클래스.
 * 이 클래스는 배치 작업 실행과 관련된 메타데이터를 데이터베이스에 저장하는 설정을 담당합니다. (batch db에 저장: 6개 테이블)
 * 여기에는 작업 실행(Job Execution), 단계 실행(Step Execution), 작업 인스턴스(Job Instance) 등의 메타데이터가 포함됩니다.
 */
@Configuration
public class BatchConfiguration extends DefaultBatchConfiguration {

    private final DataSource batchDataSource;
    private final PlatformTransactionManager batchTransactionManager;

    /**
     * BatchConfiguration 생성자.
     *
     * @param batchDataSource         배치 메타데이터를 저장할 데이터베이스 연결 정보를 포함하는 DataSource
     * @param batchTransactionManager 배치 작업의 트랜잭션을 관리할 트랜잭션 매니저
     */
    public BatchConfiguration(@Qualifier("batchDataSource") DataSource batchDataSource,
                              @Qualifier("batchTransactionManager") PlatformTransactionManager batchTransactionManager) {
        this.batchDataSource = batchDataSource;
        this.batchTransactionManager = batchTransactionManager;
    }


    /**
     * JobRepository에서 사용할 데이터 소스를 반환합니다.
     *
     * @return 배치 데이터 소스
     */
    @Override
    @NonNull
    protected DataSource getDataSource() {
        return batchDataSource;
    }


    /**
     * JobRepository에서 사용할 트랜잭션 매니저를 반환합니다.
     *
     * @return 배치 트랜잭션 매니저
     */
    @Override
    @NonNull
    @Bean
    public PlatformTransactionManager getTransactionManager() {
        return batchTransactionManager;
    }


    /**
     * JobRepository 빈을 생성하여 반환합니다.
     * JobRepository는 배치 작업 실행과 관련된 메타데이터를 데이터베이스에 저장합니다.
     *
     * @return JobRepository 인스턴스
     */
    @Override
    @Bean
    @NonNull
    @DependsOn("batchDataSource")
    public JobRepository jobRepository() {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean(); // JobRepository를 생성하는 팩토리 빈
        factory.setDataSource(batchDataSource);                            // 메타데이터를 저장할 데이터베이스 연결 정보를 설정
        factory.setTransactionManager(batchTransactionManager);            // 트랜잭션을 관리할 트랜잭션 매니저를 설정
        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");    // 트랜잭션의 격리 수준을 설정
        factory.setTablePrefix("BATCH_"); // 메타데이터를 저장할 테이블의 접두사를 설정 (예: BATCH_JOB_INSTANCE, BATCH_JOB_EXECUTION)
        try {
            factory.afterPropertiesSet();                       // 팩토리 빈의 설정을 완료
            return Objects.requireNonNull(factory.getObject()); // JobRepository 인스턴스를 생성하여 반환
        } catch (Exception e) {
            throw new RuntimeException("Could not create JobRepository", e); // JobRepository 생성 중 오류 발생 시 예외를 던짐
        }
    }

}
