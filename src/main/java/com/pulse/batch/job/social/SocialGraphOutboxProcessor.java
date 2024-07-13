package com.pulse.batch.job.social;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.social.SocialGraphOutbox;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@StepScope
@Component
public class SocialGraphOutboxProcessor implements ItemProcessor<SocialGraphOutbox, SocialGraphOutbox> {

    @Value("#{jobParameters['socialGraphParam']}")
    private String socialGraphParam;

    @Override
    public SocialGraphOutbox process(SocialGraphOutbox item) throws Exception {
        item.changeStatus(MessageStatus.PROCESSED);
        item.changeProcessedAt(LocalDateTime.now());
        return item;
    }

}
