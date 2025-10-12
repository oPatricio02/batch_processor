package com.batch.processor.controller;

import com.batch.processor.domain.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/batch")
public class BatchController {

    JobService service;

    public BatchController(JobService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> executarBatch(){
        service.executar();
        log.info("Job Iniciado as {}", LocalDateTime.now());
        return ResponseEntity.accepted().body("Job iniciado de forma assincrona com sucesso!");
    }

}
