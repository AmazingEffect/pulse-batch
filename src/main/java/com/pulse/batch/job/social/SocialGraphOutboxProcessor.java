package com.pulse.batch.job.social;

import com.pulse.batch.entity.constant.MessageStatus;
import com.pulse.batch.entity.social.SocialGraphOutbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * SocialGraphOutbox 항목을 처리하는 클래스. (각 항목의 상태를 변경하고 처리 시간을 기록합니다.)
 */
@Slf4j
@StepScope
@Component
public class SocialGraphOutboxProcessor implements ItemProcessor<SocialGraphOutbox, SocialGraphOutbox> {

    @Value("#{jobParameters['socialGraphParam']}")
    private String socialGraphParam;

    /**
     * ItemProcessor는 Item을 단건으로 처리하도록 설계되어 있다.
     *
     * @param item : ItemReader로부터 읽어온 데이터를 가지고 있습니다.
     * @return : 가공된 아이템을 반환합니다.
     */
    @Override
    public SocialGraphOutbox process(SocialGraphOutbox item) {
        log.info("Social ItemProcessor 동작");

        // 1. 항목의 상태를 재발행으로 변경
        item.changeStatus(MessageStatus.REISSUE);

        // 2. 항목의 처리 시간을 현재 시간으로 설정
        item.changeProcessedAt(LocalDateTime.now());

        return item;
    }

}
