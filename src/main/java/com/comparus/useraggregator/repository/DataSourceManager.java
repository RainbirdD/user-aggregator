package com.comparus.useraggregator.repository;

import com.comparus.useraggregator.configuration.DataSourcesConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class DataSourceManager {

    private final Map<String, DataSource> dataSources = new HashMap<>();
    private final DataSourcesConfig config;

    public DataSourceManager(DataSourcesConfig config) {
        this.config = config;
        initializeDataSources();
    }

    private void initializeDataSources() {
        for (DataSourcesConfig.DataSourceProperties props : config.getDataSources()) {
            DataSource dataSource = createDataSource(props);
            dataSources.put(props.getName(), dataSource);
        }
    }

    private DataSource createDataSource(DataSourcesConfig.DataSourceProperties props) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(props.getUrl());
        hikariConfig.setUsername(props.getUser());
        hikariConfig.setPassword(props.getPassword());

        switch (props.getStrategy().toLowerCase()) {
            case "postgres":
                hikariConfig.setDriverClassName("org.postgresql.Driver");
                break;
            case "mysql":
                hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
                break;
            default:
                throw new IllegalArgumentException("Unsupported strategy: " + props.getStrategy());
        }

        return new HikariDataSource(hikariConfig);
    }
}
