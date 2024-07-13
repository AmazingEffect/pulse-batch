package com.pulse.batch.job;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.member.MemberOutbox;
import com.pulse.batch.repository.member.MemberOutboxRepository;
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
public class MemberOutboxJobConfig {

    private final MemberOutboxRepository memberOutboxRepository;

    @Bean
    public Job processMemberOutboxJob(
            JobRepository jobRepository,
            Step processMemberOutboxStep
    ) {
        return new JobBuilder("processMemberOutboxJob", jobRepository)
                .start(processMemberOutboxStep)
                .build();
    }


    @Bean
    @JobScope
    public Step processMemberOutboxStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['memberParam']}") String memberParam
    ) {
        return new StepBuilder("processMemberOutboxStep", jobRepository)
                .<MemberOutbox, MemberOutbox>chunk(10, transactionManager)
                .reader(memberOutboxReader())
                .processor(memberOutboxProcessor(memberParam))
                .writer(memberOutboxWriter())
                .build();
    }


    @Bean
    @StepScope
    public ItemReader<MemberOutbox> memberOutboxReader() {
        List<MemberOutbox> items = memberOutboxRepository.findByStatus(MessageStatus.FAIL);
        return new ListItemReader<>(items);
    }


    @Bean
    @StepScope
    public ItemProcessor<MemberOutbox, MemberOutbox> memberOutboxProcessor(
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
    public ItemWriter<MemberOutbox> memberOutboxWriter() {
        return items -> items.forEach(item -> log.info("Processed member outbox item: {}", item));
    }

}
