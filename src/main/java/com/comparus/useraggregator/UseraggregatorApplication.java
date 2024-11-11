package com.comparus.useraggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class UseraggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(UseraggregatorApplication.class, args);
    }

}
