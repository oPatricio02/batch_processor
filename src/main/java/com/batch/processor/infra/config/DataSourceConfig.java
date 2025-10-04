package com.batch.processor.infra.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final AppDataSourceProperties appDataSourceProperties;
    private final BatchDataSourceProperties batchDataSourceProperties;

    public DataSourceConfig(AppDataSourceProperties appDataSourceProperties,
                            BatchDataSourceProperties batchDataSourceProperties) {
        this.appDataSourceProperties = appDataSourceProperties;
        this.batchDataSourceProperties = batchDataSourceProperties;
    }

    // ========== DataSource da Aplicação (Principal) ==========
    @Primary
    @Bean(name = "appDataSource")
    public DataSource appDataSource() {
        return this.appDataSourceProperties.createDataSource();
    }

    @Primary
    @Bean(name = "appTransactionManager")
    public PlatformTransactionManager appTransactionManager(@Qualifier("appDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // ========== DataSource do Spring Batch (Separado) ==========
    @Bean(name = "novoDataSource")
    @BatchDataSource
    public DataSource batchDataSource() {
        return this.batchDataSourceProperties.createDataSource();
    }

    @Bean(name = "novoTransactionManager")
    public PlatformTransactionManager batchTransactionManager(@Qualifier("novoDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
