package com.batch.processor.controller;

import com.batch.processor.domain.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/batch")
public class BatchController {

    JobService service;
    private static final Logger MONITOR_LOG = LoggerFactory.getLogger("MONITORAMENTO_BATCH");

    public BatchController(JobService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> executarBatch(){
        MONITOR_LOG.info("Job Iniciado as {}", LocalDateTime.now());
        service.executar();
        return ResponseEntity.accepted().body("Job iniciado de forma assincrona com sucesso!");
    }

}
