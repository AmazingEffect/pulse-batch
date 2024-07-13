package com.pulse.batch.job.param;

import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Job 파라미터를 사용하는 컴포넌트
 * JobParameters는 배치 잡이 실행될 때 전달되는 파라미터를 담고 있는 객체입니다.
 */
@Component
@StepScope
public class MyJobComponent {

    @Value("#{jobParameters['myParameter']}")
    private String myParameter;

    @PostConstruct
    public void init() {
        // myParameter를 사용한 초기화 작업
        System.out.println("Job parameter myParameter: " + myParameter);
    }

}
