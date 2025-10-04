package com.batch.processor.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
@Slf4j
public class JobLogListener implements JobExecutionListener, StepExecutionListener {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step '{}' finalizado - Lidos: {}, Escritos: {}, Pulados: {}, Tempo: {} ms, Status: {}",
                stepExecution.getStepName(),
                stepExecution.getReadCount(),
                stepExecution.getWriteCount(),
                stepExecution.getSkipCount(),
                timePerStep(stepExecution.getStartTime(), stepExecution.getEndTime()),
                stepExecution.getExitStatus().getExitCode());

        return stepExecution.getExitStatus();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        var duracao = ChronoUnit.SECONDS.between(Objects.requireNonNull(jobExecution.getStartTime()), jobExecution.getEndTime());

        log.info("Job finalizado - Status: {} , Tempo de execução: {} segundos", jobExecution.getStatus(),duracao);
    }

    private Integer timePerStep(LocalDateTime start, LocalDateTime end){
        if(start != null && end != null){
            return (int) ChronoUnit.MILLIS.between(start, end);
        }
        return null;
    }
}
