package com.xmonster.howtaxing_admin.config;

import com.xmonster.howtaxing_admin.dto.house.HousePubLandPriceInfoDto;
import com.xmonster.howtaxing_admin.utils.CsvReader;
import com.xmonster.howtaxing_admin.utils.CsvWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileItemReaderJobConfig {
    // TODO. 추후 CSV 등록이 필요한 시점에 수정 예정
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CsvReader csvReader;
    private final CsvWriter csvWriter;

    private static final int chunkSize = 1000;

    @Bean
    public TaskExecutor taskExecutor() {
        // csv 처리 속도를 위한 스레드 추가
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(30);
        taskExecutor.afterPropertiesSet();

        return taskExecutor;
    }

    @Bean
    public Job csvFileItemReaderJob() throws Exception{
        return jobBuilderFactory.get("csvFileItemReaderJob")
                //.incrementer(new RunIdIncrementer())
                .start(csvFileItemReaderStep())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        // TODO. 배치 선처리 필요시 추가
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        // 배치수행 종료 시 상태가 UNKNOWN 이면 강제로 FAILED 상태로 변경
                        if (jobExecution.getStatus() == BatchStatus.UNKNOWN) {
                            jobExecution.setStatus(BatchStatus.FAILED);

                            Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
                            for(StepExecution stepExecution : stepExecutions) {
                                if(stepExecution.getStatus() == BatchStatus.UNKNOWN) {
                                    stepExecution.setStatus(BatchStatus.FAILED);
                                }
                            }
                        }
                    }
                })
                .build();
    }

    @Bean
    public Step csvFileItemReaderStep() throws Exception{
        return stepBuilderFactory.get("csvFileItemReaderStep")
                .<HousePubLandPriceInfoDto, HousePubLandPriceInfoDto>chunk(chunkSize)
                .reader(csvReader.csvFileItemReader())
                .writer(csvWriter)
                .taskExecutor(taskExecutor())       // 배치 속도를 위해 스레드처리 추가
                .throttleLimit(5)  
                .build();
    }
}
