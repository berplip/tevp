package com.example.Microservice_Usuario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

/**
 * Configuración para HATEOAS.
 * Habilita el soporte para hypermedia HAL.
 */
@Configuration
@EnableHypermediaSupport(type = HypermediaType.HAL)
public class HateoasConfig {
    // Spring Boot manejará automáticamente la configuración de HATEOAS
}
