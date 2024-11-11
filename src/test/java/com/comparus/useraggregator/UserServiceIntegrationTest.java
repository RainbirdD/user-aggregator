package com.comparus.useraggregator;

import com.comparus.useraggregator.dto.User;
import com.comparus.useraggregator.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("postgres_db_test")
            .withUsername("postgres_user")
            .withPassword("postgres_pass");

    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("mysql_db_test")
            .withUsername("mysql_user")
            .withPassword("mysql_pass");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        postgresContainer.start();
        mysqlContainer.start();

        registry.add("POSTGRES_DB_URL", postgresContainer::getJdbcUrl);
        registry.add("POSTGRES_DB_USER", postgresContainer::getUsername);
        registry.add("POSTGRES_DB_PASSWORD", postgresContainer::getPassword);

        registry.add("MYSQL_DB_URL", mysqlContainer::getJdbcUrl);
        registry.add("MYSQL_DB_USER", mysqlContainer::getUsername);
        registry.add("MYSQL_DB_PASSWORD", mysqlContainer::getPassword);

        initializePostgresDatabase();
        initializeMySqlDatabase();
    }

    private static void initializePostgresDatabase() {
        try (var connection = postgresContainer.createConnection("")) {
            var statement = connection.createStatement();
            statement.execute("CREATE TABLE users (user_id SERIAL PRIMARY KEY, username VARCHAR(50), first_name VARCHAR(50), last_name VARCHAR(50));");
            statement.execute("INSERT INTO users (username, first_name, last_name) VALUES ('user-1', 'User', 'Userenko'), ('user-2', 'Testuser', 'Testov');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initializeMySqlDatabase() {
        try (var connection = mysqlContainer.createConnection("")) {
            var statement = connection.createStatement();
            statement.execute("CREATE TABLE user_table (id INT AUTO_INCREMENT PRIMARY KEY, login VARCHAR(50), name VARCHAR(50), surname VARCHAR(50));");
            statement.execute("INSERT INTO user_table (login, name, surname) VALUES ('user-1', 'Vanilla', 'Ice'), ('user-2', 'White', 'Album');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public void tearDown() {
        postgresContainer.stop();
        mysqlContainer.stop();
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getAllUsers(Optional.empty(), Optional.empty(), Optional.empty());
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }
}
