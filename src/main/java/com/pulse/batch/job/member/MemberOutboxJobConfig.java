package com.pulse.batch.job.member;

import com.pulse.batch.entity.member.MemberOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class MemberOutboxJobConfig {

    private final MemberOutboxReader memberOutboxReader;
    private final MemberOutboxProcessor memberOutboxProcessor;
    private final MemberOutboxWriter memberOutboxWriter;

    @Bean
    public Job processMemberOutboxJob(
            JobRepository jobRepository,
            Step processMemberOutboxStep
    ) {
        return new JobBuilder("processMemberOutboxJob", jobRepository)
                .start(processMemberOutboxStep)
                .build();
    }


    /**
     * JobScope 어노테이션은 주로 Step을 구성하는 데 사용됩니다.
     *
     * @param jobRepository      JobRepository
     * @param transactionManager PlatformTransactionManager
     * @param memberParam        memberParam
     * @return Step
     */
    @Transactional
    @Bean
    @JobScope
    public Step processMemberOutboxStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['memberParam']}") String memberParam
    ) {
        return new StepBuilder("processMemberOutboxStep", jobRepository)
                .<MemberOutbox, MemberOutbox>chunk(10, transactionManager)
                .reader(memberOutboxReader)
                .processor(memberOutboxProcessor)
                .writer(memberOutboxWriter)
                .build();
    }

}
