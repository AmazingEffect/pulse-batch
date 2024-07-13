package com.pulse.batch.job;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.social.SocialGraphOutbox;
import com.pulse.batch.repository.social.SocialGraphOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
@Slf4j
public class SocialGraphOutboxJobConfig {

    private final SocialGraphOutboxRepository socialGraphOutboxRepository;

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
                .reader(socialGraphOutboxReader())
                .processor(socialGraphOutboxProcessor(socialGraphParam))
                .writer(socialGraphOutboxWriter())
                .build();
    }


    @Bean
    @StepScope
    public ItemReader<SocialGraphOutbox> socialGraphOutboxReader() {
        List<SocialGraphOutbox> items = socialGraphOutboxRepository.findByStatus(MessageStatus.FAIL);
        return new ListItemReader<>(items);
    }


    @Bean
    @StepScope
    public ItemProcessor<SocialGraphOutbox, SocialGraphOutbox> socialGraphOutboxProcessor(
            @Value("#{jobParameters['param']}") String param
    ) {
        return item -> {
            // 파라미터를 사용하여 추가적인 로직을 구현할 수 있습니다.
            item.changeStatus(MessageStatus.PROCESSED);
            item.changeProcessedAt(LocalDateTime.now());
            return item;
        };
    }


    @Bean
    @StepScope
    public ItemWriter<SocialGraphOutbox> socialGraphOutboxWriter() {
        return items -> items.forEach(item -> log.info("Processed social graph outbox item: {}", item));
    }

}
