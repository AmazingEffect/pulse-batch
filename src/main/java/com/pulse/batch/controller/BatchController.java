package com.pulse.batch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job processMemberOutboxJob;
    private final Job processContentOutboxJob;
    private final Job processSocialGraphOutboxJob;

    @GetMapping("/run-member-job")
    public String runMemberJob(@RequestParam("param") String param) throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("param", param)
                .toJobParameters();
        jobLauncher.run(processMemberOutboxJob, jobParameters);
        return "Member job started";
    }


    @GetMapping("/run-content-job")
    public String runContentJob(@RequestParam("param") String param) throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("param", param)
                .toJobParameters();
        jobLauncher.run(processContentOutboxJob, jobParameters);
        return "Content job started";
    }


    @GetMapping("/run-social-graph-job")
    public String runSocialGraphJob(@RequestParam("param") String param) throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("param", param)
                .toJobParameters();
        jobLauncher.run(processSocialGraphOutboxJob, jobParameters);
        return "Social Graph job started";
    }

}
