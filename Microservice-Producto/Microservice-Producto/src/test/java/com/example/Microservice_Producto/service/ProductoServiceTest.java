package com.example.Microservice_Producto.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Microservice_Producto.model.Producto;
import com.example.Microservice_Producto.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Smartphone Samsung");
        producto.setDescripcion("Smartphone de última generación");
        producto.setPrecio(800000.0);
        producto.setStock(15);
    }

    @Test
    void deberiaObtenerTodosLosProductos() {
        // Given
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findAll()).thenReturn(productos);

        // When
        List<Producto> resultado = productoService.obtenerTodos();

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Smartphone Samsung");
        verify(productoRepository).findAll();
    }

    @Test
    void deberiaObtenerProductoPorId() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When
        Producto resultado = productoService.obtenerPorId(1L);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Smartphone Samsung");
        verify(productoRepository).findById(1L);
    }

    @Test
    void deberiaGuardarProducto() {
        // Given
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        Producto resultado = productoService.crear(producto);

        // Then
        assertThat(resultado.getNombre()).isEqualTo("Smartphone Samsung");
        assertThat(resultado.getPrecio()).isEqualTo(800000.0);
        verify(productoRepository).save(producto);
    }

    @Test
    void deberiaEliminarProducto() {
        // Given
        doNothing().when(productoRepository).deleteById(1L);

        // When
        productoService.eliminar(1L);

        // Then
        verify(productoRepository).deleteById(1L);
    }

    @Test
    void deberiaRetornarNullAlObtenerProductoInexistente() {
        // Given
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Producto resultado = productoService.obtenerPorId(999L);

        // Then
        assertThat(resultado).isNull();
        verify(productoRepository).findById(999L);
    }

    @Test
    void deberiaActualizarProducto() {
        // Given
        Producto productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Smartphone Samsung Pro");
        productoActualizado.setPrecio(950000.0);
        productoActualizado.setStock(20);

        when(productoRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        // When
        Producto resultado = productoService.actualizar(1L, productoActualizado);

        // Then
        assertThat(resultado.getNombre()).isEqualTo("Smartphone Samsung Pro");
        assertThat(resultado.getPrecio()).isEqualTo(950000.0);
        verify(productoRepository).existsById(1L);
        verify(productoRepository).save(productoActualizado);
    }
}
