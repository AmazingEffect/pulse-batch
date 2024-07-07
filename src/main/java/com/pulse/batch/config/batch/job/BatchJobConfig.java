package com.pulse.batch.config.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchJobConfig {

    @Bean
    public Job processOutboxJob(JobRepository jobRepository, Step processMemberOutboxStep, Step processContentOutboxStep, Step processSocialGraphOutboxStep) {
        return new JobBuilder("processOutboxJob", jobRepository)
                .start(processMemberOutboxStep)
                .next(processContentOutboxStep)
                .next(processSocialGraphOutboxStep)
                .build();
    }

    @Bean
    public Step processMemberOutboxStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processMemberOutboxStep", jobRepository)
                .<String, String>chunk(10, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Step processContentOutboxStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processContentOutboxStep", jobRepository)
                .<String, String>chunk(10, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Step processSocialGraphOutboxStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processSocialGraphOutboxStep", jobRepository)
                .<String, String>chunk(10, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    public ItemReader<String> itemReader() {
        return new ListItemReader<>(List.of("item1", "item2", "item3"));
    }

    public ItemProcessor<String, String> itemProcessor() {
        return item -> "Processed " + item;
    }

    public ItemWriter<String> itemWriter() {
        return items -> items.forEach(item -> log.info(item));
    }
}
