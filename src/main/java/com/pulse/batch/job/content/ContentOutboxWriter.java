package com.pulse.batch.job.content;

import com.pulse.batch.entity.content.ContentOutbox;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class ContentOutboxWriter implements ItemWriter<ContentOutbox> {

    @Override
    public void write(Chunk<? extends ContentOutbox> chunk) throws Exception {
        chunk.getItems().forEach(item -> System.out.println("Processed content outbox item: " + item));
    }

}
