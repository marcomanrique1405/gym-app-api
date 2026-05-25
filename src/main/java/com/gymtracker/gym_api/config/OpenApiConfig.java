package com.gymtracker.gym_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gymTrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gym Tracker API")
                        .version("1.0.0")
                        .description("""
                                API REST para la gestión de usuarios, rutinas de entrenamiento,
                                días de rutina, ejercicios, series, sesiones de entrenamiento
                                y dashboard personalizado.
                                """)
                        .contact(new Contact()
                                .name("Marco Manrique")
                                .url("https://github.com/marcomanrique1405/gym-app-api")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}