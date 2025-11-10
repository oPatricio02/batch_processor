package com.batch.processor;

import com.batch.processor.infra.config.AppDataSourceProperties;
import com.batch.processor.infra.config.BatchDataSourceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties({
        AppDataSourceProperties.class,
        BatchDataSourceProperties.class
})
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
