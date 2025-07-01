package com.example.Microservice_Producto;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.Microservice_Producto.model.Producto;
import com.example.Microservice_Producto.service.ProductoService;

import net.datafaker.Faker;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductoService productoService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Iniciando DataLoader en perfil DEV...");
        
        Faker faker = new Faker();
        Random random = new Random();

        // Generar productos de diferentes categorías
        String[] categorias = {"Electrónicos", "Ropa", "Hogar", "Deportes", "Libros", "Juguetes", "Alimentación"};
        
        for (int i = 0; i < 50; i++) {
            Producto producto = new Producto();
            
            // Dejar que el servicio genere el código automáticamente
            // No establecemos código aquí para que se genere automáticamente
            
            // Generar nombre del producto basado en categorías
            String categoria = categorias[random.nextInt(categorias.length)];
            String nombre = generarNombreProducto(faker, categoria);
            
            producto.setNombre(nombre);
            producto.setDescripcion(faker.lorem().sentence(10));
            
            // Generar precio realista según categoría
            Double precio = generarPrecio(categoria, random);
            producto.setPrecio(precio);
            
            // Generar stock aleatorio
            producto.setStock(faker.number().numberBetween(0, 100));
            
            productoService.crear(producto);
        }
        
        System.out.println("✅ DataLoader ejecutado: Se han generado 50 productos de prueba");
    }
    
    private String generarNombreProducto(Faker faker, String categoria) {
        return switch (categoria) {
            case "Electrónicos" -> faker.options().option(
                "Smartphone " + faker.brand().watch(),
                "Laptop " + faker.brand().watch(),
                "Tablet " + faker.brand().watch(),
                "Auriculares " + faker.brand().watch(),
                "Televisor " + faker.number().numberBetween(32, 75) + "\"",
                "Cámara Digital " + faker.brand().watch()
            );
            case "Ropa" -> faker.options().option(
                "Camiseta " + faker.color().name(),
                "Jeans " + faker.brand().watch(),
                "Chaqueta de cuero",
                "Zapatillas " + faker.brand().watch(),
                "Vestido " + faker.color().name(),
                "Suéter de lana"
            );
            case "Hogar" -> faker.options().option(
                "Sofá " + faker.number().numberBetween(2, 5) + " plazas",
                "Mesa de madera",
                "Lámpara " + faker.color().name(),
                "Alfombra " + faker.number().numberBetween(120, 300) + "cm",
                "Cojín " + faker.color().name(),
                "Estante de madera"
            );
            case "Deportes" -> faker.options().option(
                "Balón de fútbol",
                "Raqueta de tenis",
                "Pesas " + faker.number().numberBetween(5, 50) + "kg",
                "Bicicleta " + faker.brand().watch(),
                "Camiseta deportiva " + faker.team().name(),
                "Zapatillas running " + faker.brand().watch()
            );
            case "Libros" -> faker.options().option(
                "Libro: " + faker.book().title(),
                "Manual de " + faker.educator().course(),
                "Novela: " + faker.book().title(),
                "Biografía de " + faker.name().fullName(),
                "Guía de " + faker.hobby().activity(),
                "Enciclopedia de " + faker.educator().course()
            );
            case "Juguetes" -> faker.options().option(
                "Muñeca " + faker.name().firstName(),
                "Lego " + faker.number().numberBetween(100, 2000) + " piezas",
                "Peluche de " + faker.animal().name(),
                "Puzzle " + faker.number().numberBetween(500, 2000) + " piezas",
                "Robot " + faker.superhero().name(),
                "Juego de mesa clásico"
            );
            case "Alimentación" -> faker.options().option(
                faker.food().ingredient() + " orgánico",
                "Conserva de " + faker.food().fruit(),
                faker.food().spice() + " premium",
                "Aceite de " + faker.food().ingredient(),
                "Snack de " + faker.food().ingredient(),
                faker.food().dish() + " congelado"
            );
            default -> faker.commerce().productName();
        };
    }
    
    private Double generarPrecio(String categoria, Random random) {
        return switch (categoria) {
            case "Electrónicos" -> 50000.0 + (random.nextDouble() * 950000.0); // $50,000 - $1,000,000
            case "Ropa" -> 5000.0 + (random.nextDouble() * 45000.0);          // $5,000 - $50,000
            case "Hogar" -> 15000.0 + (random.nextDouble() * 185000.0);       // $15,000 - $200,000
            case "Deportes" -> 8000.0 + (random.nextDouble() * 92000.0);      // $8,000 - $100,000
            case "Libros" -> 2000.0 + (random.nextDouble() * 23000.0);        // $2,000 - $25,000
            case "Juguetes" -> 3000.0 + (random.nextDouble() * 47000.0);      // $3,000 - $50,000
            case "Alimentación" -> 500.0 + (random.nextDouble() * 9500.0);    // $500 - $10,000
            default -> 1000.0 + (random.nextDouble() * 99000.0);              // $1,000 - $100,000
        };
    }
}
