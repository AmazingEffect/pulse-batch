package com.pulse.batch.job.content;

import com.pulse.batch.entity.constant.MessageStatus;
import com.pulse.batch.entity.content.ContentOutbox;
import com.pulse.batch.repository.content.ContentOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * ContentOutbox 항목을 읽어오는 클래스. (데이터를 읽어오는 역할을 합니다.)
 * 실패한 상태의 항목들을 읽어옵니다.
 */
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
            // 실패 상태의 항목들을 읽어옴
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
