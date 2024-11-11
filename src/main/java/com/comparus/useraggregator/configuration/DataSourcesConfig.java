package com.comparus.useraggregator.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties
@Data
public class DataSourcesConfig {

    private List<DataSourceProperties> dataSources;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataSourceProperties {
        private String name;
        private String strategy;
        private String url;
        private String table;
        private String user;
        private String password;
        private Map<String, String> mapping;
    }
}
