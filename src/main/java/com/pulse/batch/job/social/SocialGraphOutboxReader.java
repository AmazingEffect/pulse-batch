package com.pulse.batch.job.social;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.social.SocialGraphOutbox;
import com.pulse.batch.repository.social.SocialGraphOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class SocialGraphOutboxReader implements ItemReader<SocialGraphOutbox> {

    private final SocialGraphOutboxRepository socialGraphOutboxRepository;

    private List<SocialGraphOutbox> items;
    private int nextItemIndex;

    @Override
    public SocialGraphOutbox read() throws Exception {
        log.info("Social ItemReader 동작!");

        if (items == null) {
            items = socialGraphOutboxRepository.findByStatus(MessageStatus.FAIL);
            nextItemIndex = 0;
        }

        SocialGraphOutbox nextItem = null;

        if (nextItemIndex < items.size()) {
            nextItem = items.get(nextItemIndex);
            nextItemIndex++;
        } else {
            items = null;
        }

        return nextItem;
    }

}
