package com.pulse.batch.job.social;

import com.pulse.batch.entity.social.SocialGraphOutbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
public class SocialGraphOutboxWriter implements ItemWriter<SocialGraphOutbox> {

    @Override
    public void write(Chunk<? extends SocialGraphOutbox> chunk) throws Exception {
        log.info("Social ItemWriter 동작");
        chunk.getItems().forEach(item -> System.out.println("Processed social graph outbox item: " + item));
    }

}
