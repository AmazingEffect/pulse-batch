package com.pulse.batch.job.member;

import com.pulse.batch.entity.member.MemberOutbox;
import com.pulse.batch.kafka.KafkaProducerService;
import com.pulse.batch.repository.member.MemberOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * MemberOutbox 재발행 kafka 메시지를 발행하는 클래스. (엔티티 변경사항도 함께 적용됩니다.)
 */
@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MemberOutboxWriter implements ItemWriter<MemberOutbox> {

    private final KafkaProducerService kafkaProducerService;
    private final MemberOutboxRepository memberOutboxRepository;

    /**
     * JPA만 사용할 것이라면 JpaItemWriter를 사용하겠지만 JPA와 Kafka를 함께 사용하기 때문에 ItemWriter를 직접 구현합니다.
     *
     * @param chunk : Chunk는 ItemReader로부터 읽어온 데이터를 가지고 있습니다.
     */
    @Override
    public void write(Chunk<? extends MemberOutbox> chunk) throws Exception {
        log.info("Member ItemWriter 동작");

        for (MemberOutbox item : chunk.getItems()) {
            // Kafka 메시지 전송
            kafkaProducerService.sendWithRetry("member-re-issue", String.valueOf(item.getId()));
            log.info("Produced member outbox item: {}", item);
        }

        // 데이터베이스에 변경 사항 저장
        memberOutboxRepository.saveAll(chunk.getItems());
    }

}
