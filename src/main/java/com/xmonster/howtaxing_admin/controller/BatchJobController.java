package com.xmonster.howtaxing_admin.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BatchJobController {

    private final JobOperator jobOperator;
    private final JobLauncher jobLauncher;
    private final Job csvFileItemReaderJob;

    @PostMapping("/startJob")
    public String startJob() {
        //TODO: process POST request
        try {
            JobParameters jobParameters = new JobParameters();
            jobLauncher.run(csvFileItemReaderJob, jobParameters);
            
            return "Job started successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to start job: " + e.getMessage();
        }
    }

    @PostMapping("/stopJob")
    public String stopJob(@RequestParam Long executionId) throws Exception {
        //TODO: process POST request
        log.info(">> [Controller]BatchJobController stopJob: {} - 배치 중단", csvFileItemReaderJob.getName());
        try {
            jobOperator.stop(executionId);
            return "Job stopped successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to stop job: " + e.getMessage();
        }
    }

    @PostMapping("/restartJob")
    public String restartJob(@RequestParam Long executionId) throws Exception {
        //TODO: process POST request
        log.info(">> [Controller]BatchJobController restartJob: {} - 배치 재시작", csvFileItemReaderJob.getName());
        try {
            jobOperator.restart(executionId);
            return "Job restarted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to restart job: " + e.getMessage();
        }
    }
    
}
