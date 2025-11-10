package com.batch.processor.domain.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final Job legadoToFato;
    private final JobLauncher jobLauncher;
    private static final Logger MONITOR_LOG = LoggerFactory.getLogger("MONITORAMENTO_BATCH");

    public JobService(JobLauncher jobLauncher, Job legadoToFato){
        this.legadoToFato = legadoToFato;
        this.jobLauncher = jobLauncher;
    }

    @Async
    public void executar(){
        try{
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(legadoToFato, jobParameters);
        }catch (Exception e){
            MONITOR_LOG.error("Erro ao executar job assíncrono: {} ", e.getMessage());
        }
    }
}
