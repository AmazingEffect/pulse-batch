package com.pulse.batch.job.member;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.member.MemberOutbox;
import com.pulse.batch.repository.member.MemberOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@StepScope
@Component
public class MemberOutboxReader implements ItemReader<MemberOutbox> {

    private final MemberOutboxRepository memberOutboxRepository;

    private List<MemberOutbox> items;
    private int nextItemIndex;

    @Override
    public MemberOutbox read() throws Exception {
        if (items == null) {
            items = memberOutboxRepository.findByStatus(MessageStatus.FAIL);
            nextItemIndex = 0;
        }

        MemberOutbox nextItem = null;

        if (nextItemIndex < items.size()) {
            nextItem = items.get(nextItemIndex);
            nextItemIndex++;
        } else {
            items = null;
        }

        return nextItem;
    }

}
