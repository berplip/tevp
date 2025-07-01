package com.example.Microservice_Producto.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests unitarios para el modelo Producto")
class ProductoTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
    }

    @Test
    @DisplayName("Debería crear un producto con valores por defecto")
    void deberiaCrearProductoConValoresPorDefecto() {
        // Then
        assertThat(producto.getId()).isNull();
        assertThat(producto.getNombre()).isNull();
        assertThat(producto.getDescripcion()).isNull();
        assertThat(producto.getPrecio()).isNull();
        assertThat(producto.getStock()).isNull();
    }

    @Test
    @DisplayName("Debería asignar y obtener el ID correctamente")
    void deberiaAsignarYObtenerIdCorrectamente() {
        // Given
        Long id = 1L;

        // When
        producto.setId(id);

        // Then
        assertThat(producto.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Debería asignar y obtener el nombre correctamente")
    void deberiaAsignarYObtenerNombreCorrectamente() {
        // Given
        String nombre = "iPhone 15 Pro";

        // When
        producto.setNombre(nombre);

        // Then
        assertThat(producto.getNombre()).isEqualTo(nombre);
    }

    @Test
    @DisplayName("Debería asignar y obtener la descripción correctamente")
    void deberiaAsignarYObtenerDescripcionCorrectamente() {
        // Given
        String descripcion = "Smartphone Apple con chip A17 Pro";

        // When
        producto.setDescripcion(descripcion);

        // Then
        assertThat(producto.getDescripcion()).isEqualTo(descripcion);
    }

    @Test
    @DisplayName("Debería asignar y obtener el precio correctamente")
    void deberiaAsignarYObtenerPrecioCorrectamente() {
        // Given
        Double precio = 1200000.0;

        // When
        producto.setPrecio(precio);

        // Then
        assertThat(producto.getPrecio()).isEqualTo(precio);
    }

    @Test
    @DisplayName("Debería asignar y obtener el stock correctamente")
    void deberiaAsignarYObtenerStockCorrectamente() {
        // Given
        Integer stock = 10;

        // When
        producto.setStock(stock);

        // Then
        assertThat(producto.getStock()).isEqualTo(stock);
    }

    @Test
    @DisplayName("Debería crear un producto completo")
    void deberiaCrearProductoCompleto() {
        // Given
        Long id = 1L;
        String nombre = "Samsung Galaxy S24";
        String descripcion = "Smartphone Samsung flagship";
        Double precio = 950000.0;
        Integer stock = 5;

        // When
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);

        // Then
        assertThat(producto.getId()).isEqualTo(id);
        assertThat(producto.getNombre()).isEqualTo(nombre);
        assertThat(producto.getDescripcion()).isEqualTo(descripcion);
        assertThat(producto.getPrecio()).isEqualTo(precio);
        assertThat(producto.getStock()).isEqualTo(stock);
    }

    @Test
    @DisplayName("Debería manejar valores nulos correctamente")
    void deberiaManejarValoresNulosCorrectamente() {
        // When
        producto.setNombre(null);
        producto.setDescripcion(null);
        producto.setPrecio(null);
        producto.setStock(null);

        // Then
        assertThat(producto.getNombre()).isNull();
        assertThat(producto.getDescripcion()).isNull();
        assertThat(producto.getPrecio()).isNull();
        assertThat(producto.getStock()).isNull();
    }
}
