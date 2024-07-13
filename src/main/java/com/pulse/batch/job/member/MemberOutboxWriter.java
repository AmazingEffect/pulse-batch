package com.pulse.batch.job.member;

import com.pulse.batch.entity.member.MemberOutbox;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@StepScope
@Component
public class MemberOutboxWriter implements ItemWriter<MemberOutbox> {

    @Override
    public void write(Chunk<? extends MemberOutbox> chunk) throws Exception {
        chunk.getItems().forEach(item -> System.out.println("Processed member outbox item: " + item));
    }

}
