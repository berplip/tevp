package com.example.Microservice_Producto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Productos API")
                        .description("API REST para la gestión de productos en el sistema de microservicios")
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
                                .url("http://localhost:8087")
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("http://localhost:8088")
                                .description("Servidor de Testing")))
                .tags(List.of(
                        new Tag()
                                .name("Productos")
                                .description("Operaciones relacionadas con la gestión de productos (sin HATEOAS)"),
                        new Tag()
                                .name("Productos V2")
                                .description("Operaciones relacionadas con la gestión de productos (con HATEOAS)")));
    }
}
