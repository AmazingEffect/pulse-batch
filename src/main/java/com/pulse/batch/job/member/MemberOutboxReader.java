package com.pulse.batch.job.member;

import com.pulse.batch.entity.constant.MessageStatus;
import com.pulse.batch.entity.member.MemberOutbox;
import com.pulse.batch.repository.member.MemberOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MemberOutbox 항목을 읽어오는 클래스. (데이터를 읽어오는 역할을 합니다.)
 */
@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class MemberOutboxReader implements ItemReader<MemberOutbox> {

    private final MemberOutboxRepository memberOutboxRepository;

    private List<MemberOutbox> items;
    private int nextItemIndex;

    @Override
    public MemberOutbox read() throws Exception {
        log.info("Member ItemReader 동작!");

        if (items == null) {
            // 실패 상태의 항목들을 읽어옴
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
