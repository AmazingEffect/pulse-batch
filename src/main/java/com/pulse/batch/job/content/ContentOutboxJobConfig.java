package com.pulse.batch.job.content;

import com.pulse.batch.entity.content.ContentOutbox;
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
public class ContentOutboxJobConfig {

    private final ContentOutboxReader contentOutboxReader;
    private final ContentOutboxProcessor contentOutboxProcessor;
    private final ContentOutboxWriter contentOutboxWriter;

    @Bean
    public Job processContentOutboxJob(
            JobRepository jobRepository,
            Step processContentOutboxStep
    ) {
        return new JobBuilder("processContentOutboxJob", jobRepository)
                .start(processContentOutboxStep)
                .build();
    }


    /**
     * JobScope 어노테이션은 주로 Step을 구성하는 데 사용됩니다.
     *
     * @param jobRepository      JobRepository
     * @param transactionManager PlatformTransactionManager
     * @param contentParam       contentParam
     * @return Step
     */
    @Transactional
    @Bean
    @JobScope
    public Step processContentOutboxStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['contentParam']}") String contentParam
    ) {
        return new StepBuilder("processContentOutboxStep", jobRepository)
                .<ContentOutbox, ContentOutbox>chunk(10, transactionManager)
                .reader(contentOutboxReader)
                .processor(contentOutboxProcessor)
                .writer(contentOutboxWriter)
                .build();
    }

}
