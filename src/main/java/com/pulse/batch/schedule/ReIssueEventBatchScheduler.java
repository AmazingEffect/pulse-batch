package com.pulse.batch.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Spring Scheduler를 사용하여 5분마다 배치 잡을 실행하도록 설정
 */
@RequiredArgsConstructor
@Component
@EnableScheduling
public class ReIssueEventBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job processMemberOutboxJob;
    private final Job processContentOutboxJob;
    private final Job processSocialGraphOutboxJob;

    @Scheduled(fixedRate = 300000) // 5분(300000ms)마다 실행
    public synchronized void runMemberJob() throws Exception {
        jobLauncher.run(processMemberOutboxJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters());
    }


    @Scheduled(fixedRate = 300000) // 5분(300000ms)마다 실행
    public synchronized void runContentJob() throws Exception {
        jobLauncher.run(processContentOutboxJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters());
    }


    @Scheduled(fixedRate = 300000) // 5분(300000ms)마다 실행
    public synchronized void runSocialGraphJob() throws Exception {
        jobLauncher.run(processSocialGraphOutboxJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters());
    }

}
