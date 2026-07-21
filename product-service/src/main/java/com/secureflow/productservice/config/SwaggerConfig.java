package com.secureflow.productservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI secureFlowOpenAPI() {

        return new OpenAPI()

                .info(
                        new Info()
                                .title("SecureFlow Product Service API")
                                .description("Product Service REST APIs")
                                .version("v1.0")
                                .contact(
                                        new Contact()
                                                .name("Ruturaj")
                                                .email("ruturaj@example.com")
                                )
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                )
                )

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "Bearer Authentication",

                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Bearer Authentication")
                )

                .externalDocs(
                        new ExternalDocumentation()
                                .description("SecureFlow Documentation")
                );

    }
}
