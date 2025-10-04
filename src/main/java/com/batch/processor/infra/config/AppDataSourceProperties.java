package com.batch.processor.infra.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Data
@ConfigurationProperties(prefix = "spring.datasource.legado")
public class AppDataSourceProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    // Objeto aninhado para as propriedades do Hikari
    private Hikari hikari = new Hikari();

    @Data
    public static class Hikari {
        private String poolName;
        private int maximumPoolSize;
        private int minimumIdle;
        private long idleTimeout;
        private long connectionTimeout;
        private long leakDetectionThreshold;
        private long validationTimeout;
    }

    public HikariDataSource createDataSource() {
        HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(this.url)
                .username(this.username)
                .password(this.password)
                .driverClassName(this.driverClassName)
                .build();

        // Configura as propriedades do Hikari
        dataSource.setPoolName(this.hikari.getPoolName());
        dataSource.setMaximumPoolSize(this.hikari.getMaximumPoolSize());
        dataSource.setMinimumIdle(this.hikari.getMinimumIdle());
        dataSource.setIdleTimeout(this.hikari.getIdleTimeout());
        dataSource.setConnectionTimeout(this.hikari.getConnectionTimeout());
        dataSource.setLeakDetectionThreshold(this.hikari.getLeakDetectionThreshold());
        dataSource.setValidationTimeout(this.hikari.getValidationTimeout());

        return dataSource;
    }
}
