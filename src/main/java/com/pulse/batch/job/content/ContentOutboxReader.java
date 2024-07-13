package com.pulse.batch.job.content;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.content.ContentOutbox;
import com.pulse.batch.repository.content.ContentOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class ContentOutboxReader implements ItemReader<ContentOutbox> {

    private final ContentOutboxRepository contentOutboxRepository;

    private List<ContentOutbox> items;
    private int nextItemIndex;

    @Override
    public ContentOutbox read() throws Exception {
        log.info("Content ItemReader 동작!");

        if (items == null) {
            items = contentOutboxRepository.findByStatus(MessageStatus.FAIL);
            nextItemIndex = 0;
        }

        ContentOutbox nextItem = null;

        if (nextItemIndex < items.size()) {
            nextItem = items.get(nextItemIndex);
            nextItemIndex++;
        } else {
            items = null;
        }

        return nextItem;
    }

}
