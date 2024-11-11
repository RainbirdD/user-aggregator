package com.comparus.useraggregator.repository;

import com.comparus.useraggregator.configuration.DataSourcesConfig;
import com.comparus.useraggregator.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Log4j2
public class UserRepository {
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";

    private final DataSourceManager dataSourceManager;

    public List<User> getUsersFromAllSources(Optional<String> username, Optional<String> name, Optional<String> surname) {
        List<User> users = new ArrayList<>();
        List<DataSourcesConfig.DataSourceProperties> dataSources = dataSourceManager.getConfig().getDataSources();
        for (DataSourcesConfig.DataSourceProperties dsProps : dataSources) {
            users.addAll(getUsersFromDataSource(dsProps, username, name, surname));
        }
        return users;
    }

    private List<User> getUsersFromDataSource(DataSourcesConfig.DataSourceProperties dsProps,
                                              Optional<String> username,
                                              Optional<String> name,
                                              Optional<String> surname) {
        List<User> users = new ArrayList<>();
        try {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(
                    dataSourceManager.getDataSources().get(dsProps.getName()));

            Map<String, Object> params = new HashMap<>();
            username.ifPresent(u -> params.put(USERNAME, u));
            name.ifPresent(n -> params.put(NAME, n));
            surname.ifPresent(s -> params.put(SURNAME, s));

            String query = buildQuery(dsProps, params);

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, params);
            for (Map<String, Object> row : rows) {
                User user = mapRowToUser(row, dsProps.getMapping());
                users.add(user);
            }
        } catch (Exception e) {
            log.error("Failed to fetch users from data source {}: {}", dsProps.getName(), e.getMessage());
        }
        return users;
    }

    private String buildQuery(DataSourcesConfig.DataSourceProperties dsProps, Map<String, Object> params) {

        StringBuilder query = new StringBuilder("SELECT ");
        query.append(String.join(", ", dsProps.getMapping().values()));
        query.append(" FROM ").append(dsProps.getTable());

        if (!params.isEmpty()) {
            query.append(" WHERE ");
            List<String> conditions = new ArrayList<>();
            for (String key : params.keySet()) {
                String column = dsProps.getMapping().get(key);
                conditions.add(column + " = :" + key);
            }
            query.append(String.join(" AND ", conditions));
        }

        return query.toString();
    }

    private User mapRowToUser(Map<String, Object> row, Map<String, String> mapping) {
        User user = new User();
        mapping.forEach((key, column) -> {
            Object value = row.get(column);
            if (value != null) {
                switch (key) {
                    case ID:
                        user.setId(value.toString());
                        break;
                    case USERNAME:
                        user.setUsername(value.toString());
                        break;
                    case NAME:
                        user.setName(value.toString());
                        break;
                    case SURNAME:
                        user.setSurname(value.toString());
                        break;
                    default:
                }
            }
        });
        return user;
    }
}
