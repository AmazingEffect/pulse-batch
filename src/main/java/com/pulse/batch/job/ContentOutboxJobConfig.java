package com.pulse.batch.job;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.content.ContentOutbox;
import com.pulse.batch.repository.content.ContentOutboxRepository;
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
public class ContentOutboxJobConfig {

    private final ContentOutboxRepository contentOutboxRepository;

    @Bean
    public Job processContentOutboxJob(
            JobRepository jobRepository,
            Step processContentOutboxStep
    ) {
        return new JobBuilder("processContentOutboxJob", jobRepository)
                .start(processContentOutboxStep)
                .build();
    }


    @Bean
    @JobScope
    public Step processContentOutboxStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['contentParam']}") String contentParam
    ) {
        return new StepBuilder("processContentOutboxStep", jobRepository)
                .<ContentOutbox, ContentOutbox>chunk(10, transactionManager)
                .reader(contentOutboxReader())
                .processor(contentOutboxProcessor(contentParam))
                .writer(contentOutboxWriter())
                .build();
    }


    @Bean
    @StepScope
    public ItemReader<ContentOutbox> contentOutboxReader() {
        List<ContentOutbox> items = contentOutboxRepository.findByStatus(MessageStatus.FAIL);
        return new ListItemReader<>(items);
    }


    @Bean
    @StepScope
    public ItemProcessor<ContentOutbox, ContentOutbox> contentOutboxProcessor(
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
    public ItemWriter<ContentOutbox> contentOutboxWriter() {
        return items -> items.forEach(item -> log.info("Processed content outbox item: {}", item));
    }

}
