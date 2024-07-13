package com.pulse.batch.job.social;

import com.pulse.batch.entity.social.SocialGraphOutbox;
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

@Slf4j
@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class SocialGraphOutboxJobConfig {

    private final SocialGraphOutboxReader socialGraphOutboxReader;
    private final SocialGraphOutboxProcessor socialGraphOutboxProcessor;
    private final SocialGraphOutboxWriter socialGraphOutboxWriter;

    @Bean
    public Job processSocialGraphOutboxJob(
            JobRepository jobRepository,
            Step processSocialGraphOutboxStep
    ) {
        return new JobBuilder("processSocialGraphOutboxJob", jobRepository)
                .start(processSocialGraphOutboxStep)
                .build();
    }


    @Bean
    @JobScope
    public Step processSocialGraphOutboxStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['socialGraphParam']}") String socialGraphParam
    ) {
        return new StepBuilder("processSocialGraphOutboxStep", jobRepository)
                .<SocialGraphOutbox, SocialGraphOutbox>chunk(10, transactionManager)
                .reader(socialGraphOutboxReader)
                .processor(socialGraphOutboxProcessor)
                .writer(socialGraphOutboxWriter)
                .build();
    }

}
