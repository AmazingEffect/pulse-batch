package com.pulse.batch.job.content;

import com.pulse.batch.entity.content.ContentOutbox;
import com.pulse.batch.kafka.KafkaProducerService;
import com.pulse.batch.repository.content.ContentOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * ContentOutbox 재발행 kafka 메시지를 발행하는 클래스. (변경 감지로 엔티티 변경사항도 함께 적용됩니다.)
 * ItemWriter는 가공된 데이터를 저장하거나 출력하는 역할을 합니다. 데이터베이스에 저장하거나 파일에 기록하거나 메시지 큐에 전송할 수 있습니다.
 * write() 메서드를 통해 한 번에 여러 아이템을 받아 처리합니다.
 */
@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class ContentOutboxWriter implements ItemWriter<ContentOutbox> {

    private final KafkaProducerService kafkaProducerService;
    private final ContentOutboxRepository contentOutboxRepository;

    /**
     * JPA만 사용할 것이라면 JpaItemWriter를 사용하겠지만 JPA와 Kafka를 함께 사용하기 때문에 ItemWriter를 직접 구현합니다.
     *
     * @param chunk : Chunk는 ItemReader로부터 읽어온 데이터를 가지고 있습니다.
     */
    @Override
    public void write(Chunk<? extends ContentOutbox> chunk) {
        log.info("Content ItemWriter 동작");

        for (ContentOutbox item : chunk.getItems()) {
            // Kafka 메시지 전송 (재발행 토픽)
            kafkaProducerService.sendWithRetry("content-re-issue", String.valueOf(item.getId()));
            log.info("Produced content outbox item: {}", item);
        }

        // 데이터베이스에 변경 사항 저장
        contentOutboxRepository.saveAll(chunk.getItems());
    }

}
