package com.example.Microservice_Usuario.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Microservicio de Usuarios API")
                                                .description("API REST para la gestión de usuarios en el sistema de microservicios")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("Equipo de Desarrollo")
                                                                .email("desarrollo@empresa.com")
                                                                .url("https://empresa.com"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("https://opensource.org/licenses/MIT")))
                                .servers(List.of(
                                                new Server()
                                                                .url("http://localhost:8082")
                                                                .description("Servidor de Desarrollo"),
                                                new Server()
                                                                .url("http://localhost:8083")
                                                                .description("Servidor de Testing")))
                                .tags(List.of(
                                                new Tag()
                                                                .name("Usuarios")
                                                                .description("Operaciones relacionadas con la gestión de usuarios"),
                                                new Tag()
                                                                .name("Usuarios V2")
                                                                .description("Operaciones relacionadas con la gestión de usuarios avanzadas")));
        }
}
