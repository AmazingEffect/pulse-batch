package com.pulse.batch.job.social;

import com.pulse.batch.entity.social.SocialGraphOutbox;
import com.pulse.batch.repository.social.SocialGraphOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * SocialGraphOutbox 재발행 kafka 메시지를 발행하는 클래스. (엔티티 변경사항도 함께 적용됩니다.)
 */
@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class SocialGraphOutboxWriter implements ItemWriter<SocialGraphOutbox> {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SocialGraphOutboxRepository socialGraphOutboxRepository;

    @Override
    public void write(Chunk<? extends SocialGraphOutbox> chunk) throws Exception {
        log.info("Social ItemWriter 동작");

        for (SocialGraphOutbox item : chunk.getItems()) {
            // Kafka 메시지 전송
            kafkaTemplate.send("social-topic", item);
            log.info("Produced social outbox item: {}", item);
        }

        // 데이터베이스에 변경 사항 저장
        socialGraphOutboxRepository.saveAll(chunk.getItems());
    }

}
