package com.pulse.batch.job.member;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.member.MemberOutbox;
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
public class MemberOutboxProcessor implements ItemProcessor<MemberOutbox, MemberOutbox> {

    @Value("#{jobParameters['memberParam']}")
    private String memberParam;

    @Override
    public MemberOutbox process(MemberOutbox item) throws Exception {
        log.info("Member ItemProcessor 동작");

        item.changeStatus(MessageStatus.PROCESSED);
        item.changeProcessedAt(LocalDateTime.now());
        return item;
    }

}
