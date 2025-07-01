package com.example.Microservice_Producto.repository;

import com.example.Microservice_Producto.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        productoRepository.deleteAll();
        
        // Crear productos de prueba
        producto1 = new Producto();
        producto1.setNombre("Laptop Gaming");
        producto1.setDescripcion("Laptop para juegos de alta gama");
        producto1.setPrecio(1500000.0);
        producto1.setStock(10);

        producto2 = new Producto();
        producto2.setNombre("Mouse Inalámbrico");
        producto2.setDescripcion("Mouse ergonómico inalámbrico");
        producto2.setPrecio(25000.0);
        producto2.setStock(50);
    }

    @Test
    void deberiaGuardarProducto() {
        // When
        Producto productoGuardado = productoRepository.save(producto1);

        // Then
        assertThat(productoGuardado.getId()).isNotNull();
        assertThat(productoGuardado.getNombre()).isEqualTo("Laptop Gaming");
        assertThat(productoGuardado.getPrecio()).isEqualTo(1500000.0);
    }

    @Test
    void deberiaEncontrarProductoPorId() {
        // Given
        Producto productoGuardado = productoRepository.save(producto1);

        // When
        Optional<Producto> productoEncontrado = productoRepository.findById(productoGuardado.getId());

        // Then
        assertThat(productoEncontrado).isPresent();
        assertThat(productoEncontrado.get().getNombre()).isEqualTo("Laptop Gaming");
    }

    @Test
    void deberiaEncontrarTodosLosProductos() {
        // Given
        productoRepository.save(producto1);
        productoRepository.save(producto2);

        // When
        List<Producto> productos = productoRepository.findAll();

        // Then
        assertThat(productos).hasSize(2);
        assertThat(productos).extracting(Producto::getNombre)
                .containsExactlyInAnyOrder("Laptop Gaming", "Mouse Inalámbrico");
    }

    @Test
    void deberiaEliminarProducto() {
        // Given
        Producto productoGuardado = productoRepository.save(producto1);
        Long productoId = productoGuardado.getId();

        // When
        productoRepository.deleteById(productoId);

        // Then
        Optional<Producto> productoEliminado = productoRepository.findById(productoId);
        assertThat(productoEliminado).isEmpty();
    }

    @Test
    void deberiaActualizarProducto() {
        // Given
        Producto productoGuardado = productoRepository.save(producto1);

        // When
        productoGuardado.setNombre("Laptop Gaming Pro");
        productoGuardado.setPrecio(1800000.0);
        Producto productoActualizado = productoRepository.save(productoGuardado);

        // Then
        assertThat(productoActualizado.getNombre()).isEqualTo("Laptop Gaming Pro");
        assertThat(productoActualizado.getPrecio()).isEqualTo(1800000.0);
    }
}
