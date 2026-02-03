package com.polaris.HS.Code.Validator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hscodeApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("HS Code Validation API")
                        .description("HS code validation, search, and national extensions")
                        .version("1.0.0"));
    }
}
