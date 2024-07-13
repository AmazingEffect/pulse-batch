package com.pulse.batch.job.member;

import com.pulse.batch.entity.member.MemberOutbox;
import com.pulse.batch.repository.member.MemberOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * MemberOutbox 재발행 kafka 메시지를 발행하는 클래스. (엔티티 변경사항도 함께 적용됩니다.)
 */
@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MemberOutboxWriter implements ItemWriter<MemberOutbox> {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MemberOutboxRepository memberOutboxRepository;

    @Override
    public void write(Chunk<? extends MemberOutbox> chunk) throws Exception {
        log.info("Member ItemWriter 동작");

        for (MemberOutbox item : chunk.getItems()) {
            // Kafka 메시지 전송
            kafkaTemplate.send("member-topic", item);
            log.info("Produced member outbox item: {}", item);
        }

        // 데이터베이스에 변경 사항 저장
        memberOutboxRepository.saveAll(chunk.getItems());
    }

}
