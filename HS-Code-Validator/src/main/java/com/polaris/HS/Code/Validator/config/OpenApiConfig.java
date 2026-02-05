package com.polaris.HS.Code.Validator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${hscode.swagger.title}")
    private String title;

    @Value("${hscode.swagger.description}")
    private String description;

    @Value("${hscode.server-url:}")
    private String serverUrl;

    @Value("${server.port}")
    private String port;

    @Value("${spring.profiles.active:local}")
    private String profile;

    @Bean
    public OpenAPI customOpenAPI() {

        String resolvedServer = switch (profile) {
            case "production" -> serverUrl;
            case "local", "docker" -> "http://localhost:" + port;
            default -> "";
        };

        return new OpenAPI()
                .servers(List.of(new Server().url(resolvedServer)))
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version("1.0.0"));
    }
}