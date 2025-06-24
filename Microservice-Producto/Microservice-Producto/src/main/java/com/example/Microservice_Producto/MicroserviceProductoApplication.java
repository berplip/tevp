package com.example.Microservice_Producto;

import com.example.Microservice_Producto.model.Producto;
import com.example.Microservice_Producto.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class MicroserviceProductoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceProductoApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(ProductoRepository repository) {
		return args -> {
			Producto p1 = new Producto();
			p1.setNombre("Laptop Pro 15");
			p1.setDescripcion("Laptop de alto rendimiento con 16GB RAM y 512GB SSD.");
			p1.setPrecio(950000.0);
			p1.setStock(25);

			Producto p2 = new Producto();
			p2.setNombre("Mouse Inalámbrico Ergonómico");
			p2.setDescripcion("Mouse óptico con 6 botones y diseño ergonómico.");
			p2.setPrecio(25000.0);
			p2.setStock(150);

			Producto p3 = new Producto();
			p3.setNombre("Monitor Curvo 27 pulgadas");
			p3.setDescripcion("Monitor QHD (1440p) con tasa de refresco de 144Hz.");
			p3.setPrecio(320000.0);
			p3.setStock(40);

			repository.saveAll(Arrays.asList(p1, p2, p3));
			System.out.println("---- Base de datos inicializada con 3 productos. ----");
		};
	}
}