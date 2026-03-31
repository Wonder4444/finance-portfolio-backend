package com.wonder4.financeportfoliobackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Finance Portfolio Backend API")
                                .description(
                                        "API documentation for the Finance Portfolio Backend application.")
                                .version("1.0.0"));
    }
}
