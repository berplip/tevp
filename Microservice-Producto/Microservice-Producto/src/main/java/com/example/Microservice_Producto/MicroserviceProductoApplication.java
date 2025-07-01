package com.example.Microservice_Producto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    org.springdoc.core.configuration.SpringDocHateoasConfiguration.class
})
public class MicroserviceProductoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceProductoApplication.class, args);
	}

}
