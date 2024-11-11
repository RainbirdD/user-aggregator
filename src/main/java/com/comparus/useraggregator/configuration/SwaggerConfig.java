package com.comparus.useraggregator.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI userAggregatorOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("User Aggregator API")
                        .description("Service for aggregating users data from multiple databases")
                        .version("v1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Repository")
                        .url("https://github.com/RainbirdD/user-aggregator"));
    }
}
