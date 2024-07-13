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

/**
 * ContentOutbox 항목을 처리하는 클래스. (각 항목의 상태를 변경하고 처리 시간을 기록합니다.)
 * ItemProcessor는 읽어온 데이터를 가공하거나 변환하는 역할을 합니다. 필터링, 데이터 변환, 데이터 검증 등의 작업을 수행할 수 있습니다.
 * process() 메서드를 통해 하나의 아이템을 입력받아 가공된 아이템을 반환합니다. ItemProcessor는 선택 사항이며 필요하지 않은 경우 생략할 수 있습니다.
 */
@Slf4j
@StepScope
@Component
public class ContentOutboxProcessor implements ItemProcessor<ContentOutbox, ContentOutbox> {

    @Value("#{jobParameters['contentParam']}")
    private String contentParam;

    @Override
    public ContentOutbox process(ContentOutbox item) throws Exception {
        log.info("Content ItemProcessor 동작");

        // 1. 항목의 상태를 재발행으로 변경
        item.changeStatus(MessageStatus.REISSUE);

        // 2. 항목의 처리 시간을 현재 시간으로 설정
        item.changeProcessedAt(LocalDateTime.now());

        return item;
    }

}
