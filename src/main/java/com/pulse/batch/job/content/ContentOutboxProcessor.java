package com.pulse.batch.job.content;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.content.ContentOutbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@StepScope
@Component
public class ContentOutboxProcessor implements ItemProcessor<ContentOutbox, ContentOutbox> {

    @Value("#{jobParameters['contentParam']}")
    private String contentParam;

    @Override
    public ContentOutbox process(ContentOutbox item) throws Exception {
        log.info("Content ItemProcessor 동작");

        item.changeStatus(MessageStatus.PROCESSED);
        item.changeProcessedAt(LocalDateTime.now());
        return item;
    }

}
