package com.pulse.batch.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Spring Scheduler를 사용하여 5분마다 배치 잡을 실행하도록 설정
 */
@Slf4j
@RequiredArgsConstructor
@Component
@EnableScheduling
public class ReIssueEventBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job processMemberOutboxJob;
    private final Job processContentOutboxJob;
    private final Job processSocialGraphOutboxJob;


    @Scheduled(fixedRate = 300000) // 5분(300000ms)마다 실행
    public synchronized void runMemberJob() {
        try {
            jobLauncher.run(processMemberOutboxJob, new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
        } catch (JobExecutionAlreadyRunningException |
                 JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error("Error running processMemberOutboxJob", e);
        }
    }


    @Scheduled(fixedRate = 300000) // 5분(300000ms)마다 실행
    public synchronized void runContentJob() {
        try {
            jobLauncher.run(processContentOutboxJob, new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
        } catch (JobExecutionAlreadyRunningException |
                 JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error("Error running processContentOutboxJob", e);
        }
    }


    @Scheduled(fixedRate = 300000) // 5분(300000ms)마다 실행
    public synchronized void runSocialGraphJob() {
        try {
            jobLauncher.run(processSocialGraphOutboxJob, new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
        } catch (JobExecutionAlreadyRunningException |
                 JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error("Error running processSocialGraphOutboxJob", e);
        }
    }

}
