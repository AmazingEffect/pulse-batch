package com.pulse.batch.job.social;

import com.pulse.batch.entity.social.SocialGraphOutbox;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class SocialGraphOutboxWriter implements ItemWriter<SocialGraphOutbox> {

    @Override
    public void write(Chunk<? extends SocialGraphOutbox> chunk) throws Exception {
        chunk.getItems().forEach(item -> System.out.println("Processed social graph outbox item: " + item));
    }

}
