package com.batch.processor.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component

public class JobLogListener implements JobExecutionListener, ChunkListener {

    private static final Logger MONITOR_LOG = LoggerFactory.getLogger("MONITORAMENTO_BATCH");

    @Override
    public void afterChunk(ChunkContext context) {
        long lidos = context.getStepContext().getStepExecution().getReadCount();
        long escritos = context.getStepContext().getStepExecution().getWriteCount();

        MONITOR_LOG.info("Progresso parcial - Lidos: {}, Escritos: {}", lidos, escritos );
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        var duracao = ChronoUnit.SECONDS.between(Objects.requireNonNull(jobExecution.getStartTime()), jobExecution.getEndTime());

        MONITOR_LOG.info("Job finalizado - Status: {} , Tempo de execução: {} segundos", jobExecution.getStatus(),duracao);
    }

    private Integer timePerStep(LocalDateTime start, LocalDateTime end){
        if(start != null && end != null){
            return (int) ChronoUnit.MILLIS.between(start, end);
        }
        return null;
    }
}
