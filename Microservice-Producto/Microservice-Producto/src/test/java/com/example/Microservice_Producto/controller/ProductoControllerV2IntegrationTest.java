package com.example.Microservice_Producto.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.Microservice_Producto.model.Producto;
import com.example.Microservice_Producto.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DisplayName("Tests completos del Controlador de Productos - API v1 y v2")
class ProductoControllerV2IntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        
        // Limpiar la base de datos antes de cada test
        productoRepository.deleteAll();
    }

    @Test
    void deberiaObtenerTodosLosProductosConHATEOAS() throws Exception {
        // Given
        Producto producto1 = new Producto();
        producto1.setNombre("iPhone 15");
        producto1.setDescripcion("Smartphone Apple");
        producto1.setPrecio(1200000.0);
        producto1.setStock(5);

        Producto producto2 = new Producto();
        producto2.setNombre("MacBook Pro");
        producto2.setDescripcion("Laptop Apple");
        producto2.setPrecio(2500000.0);
        producto2.setStock(3);

        productoRepository.save(producto1);
        productoRepository.save(producto2);

        // When & Then
        mockMvc.perform(get("/api/v2/productos")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.productoList[0].nombre", is("iPhone 15")))
                .andExpect(jsonPath("$._embedded.productoList[1].nombre", is("MacBook Pro")))
                // Verificar enlaces HATEOAS
                .andExpect(jsonPath("$._links.self.href", containsString("/api/v2/productos")))
                .andExpect(jsonPath("$._embedded.productoList[0]._links.self.href", containsString("/api/v2/productos/")))
                .andExpect(jsonPath("$._embedded.productoList[0]._links.productos.href", containsString("/api/v2/productos")));
    }

    @Test
    void deberiaObtenerProductoPorIdConHATEOAS() throws Exception {
        // Given
        Producto producto = new Producto();
        producto.setNombre("iPad Air");
        producto.setDescripcion("Tablet Apple");
        producto.setPrecio(800000.0);
        producto.setStock(10);

        Producto productoGuardado = productoRepository.save(producto);

        // When & Then
        mockMvc.perform(get("/api/v2/productos/" + productoGuardado.getId())
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombre", is("iPad Air")))
                .andExpect(jsonPath("$.precio", is(800000.0)))
                // Verificar enlaces HATEOAS
                .andExpect(jsonPath("$._links.self.href", containsString("/api/v2/productos/" + productoGuardado.getId())))
                .andExpect(jsonPath("$._links.productos.href", containsString("/api/v2/productos")))
                .andExpect(jsonPath("$._links.update.href", containsString("/api/v2/productos/" + productoGuardado.getId())))
                .andExpect(jsonPath("$._links.delete.href", containsString("/api/v2/productos/" + productoGuardado.getId())));
    }

    @Test
    void deberiaCrearNuevoProductoConHATEOAS() throws Exception {
        // Given
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("AirPods Pro");
        nuevoProducto.setDescripcion("Auriculares inalámbricos Apple");
        nuevoProducto.setPrecio(300000.0);
        nuevoProducto.setStock(20);

        // When & Then
        mockMvc.perform(post("/api/v2/productos")
                .contentType(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(nuevoProducto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombre", is("AirPods Pro")))
                .andExpect(jsonPath("$.precio", is(300000.0)))
                .andExpect(jsonPath("$.id", notNullValue()))
                // Verificar enlaces HATEOAS
                .andExpect(jsonPath("$._links.self.href", containsString("/api/v2/productos/")))
                .andExpect(jsonPath("$._links.productos.href", containsString("/api/v2/productos")));
    }

    @Test
    void deberiaActualizarProductoConHATEOAS() throws Exception {
        // Given
        Producto producto = new Producto();
        producto.setNombre("Apple Watch");
        producto.setDescripcion("Smartwatch Apple");
        producto.setPrecio(400000.0);
        producto.setStock(15);

        Producto productoGuardado = productoRepository.save(producto);

        // Producto actualizado
        productoGuardado.setNombre("Apple Watch Ultra");
        productoGuardado.setPrecio(900000.0);
        productoGuardado.setStock(12);

        // When & Then
        mockMvc.perform(put("/api/v2/productos/" + productoGuardado.getId())
                .contentType(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(productoGuardado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombre", is("Apple Watch Ultra")))
                .andExpect(jsonPath("$.precio", is(900000.0)))
                // Verificar enlaces HATEOAS
                .andExpect(jsonPath("$._links.self.href", containsString("/api/v2/productos/" + productoGuardado.getId())))
                .andExpect(jsonPath("$._links.productos.href", containsString("/api/v2/productos")));
    }

    @Test
    void deberiaEliminarProductoConHATEOAS() throws Exception {
        // Given
        Producto producto = new Producto();
        producto.setNombre("Mac Studio");
        producto.setDescripcion("Computadora de alto rendimiento Apple");
        producto.setPrecio(2800000.0);
        producto.setStock(4);

        Producto productoGuardado = productoRepository.save(producto);

        // When & Then
        mockMvc.perform(delete("/api/v2/productos/" + productoGuardado.getId()))
                .andExpect(status().isNoContent());

        // Verificar que el producto fue eliminado
        mockMvc.perform(get("/api/v2/productos/" + productoGuardado.getId())
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaBuscarProductosPorRangoPreciosConHATEOAS() throws Exception {
        // Given
        Producto producto1 = new Producto();
        producto1.setNombre("Producto Barato");
        producto1.setDescripcion("Producto económico");
        producto1.setPrecio(50000.0);
        producto1.setStock(10);

        Producto producto2 = new Producto();
        producto2.setNombre("Producto Caro");
        producto2.setDescripcion("Producto premium");
        producto2.setPrecio(200000.0);
        producto2.setStock(5);

        Producto producto3 = new Producto();
        producto3.setNombre("Producto Medio");
        producto3.setDescripcion("Producto gama media");
        producto3.setPrecio(100000.0);
        producto3.setStock(8);

        productoRepository.save(producto1);
        productoRepository.save(producto2);
        productoRepository.save(producto3);

        // When & Then - Buscar productos entre 80000 y 150000
        mockMvc.perform(get("/api/v2/productos/precio-entre")
                .param("precioMin", "80000")
                .param("precioMax", "150000")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productoList[0].nombre", is("Producto Medio")))
                // Verificar enlaces HATEOAS
                .andExpect(jsonPath("$._links.self.href", containsString("/api/v2/productos/precio-entre")))
                .andExpect(jsonPath("$._links['todos-productos'].href", containsString("/api/v2/productos")));
    }

    // ==========================================
    // TESTS PARA API v1 (/producto) - SIN HATEOAS
    // ==========================================

    @Test
    @DisplayName("Debe obtener todos los productos - API v1")
    void deberiaObtenerTodosLosProductosV1() throws Exception {
        // Given
        Producto producto1 = new Producto();
        producto1.setNombre("iPhone 14");
        producto1.setDescripcion("Smartphone Apple v1");
        producto1.setPrecio(1100000.0);
        producto1.setStock(8);

        Producto producto2 = new Producto();
        producto2.setNombre("MacBook Pro 14");
        producto2.setDescripcion("Laptop Apple v1");
        producto2.setPrecio(2300000.0);
        producto2.setStock(5);

        productoRepository.save(producto1);
        productoRepository.save(producto2);

        // When & Then
        mockMvc.perform(get("/producto"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre").value("iPhone 14"))
                .andExpect(jsonPath("$[1].nombre").value("MacBook Pro 14"));
    }

    @Test
    @DisplayName("Debe obtener producto por ID - API v1")
    void deberiaObtenerProductoPorIdV1() throws Exception {
        // Given
        Producto producto = new Producto();
        producto.setNombre("iPad mini");
        producto.setDescripcion("Tablet Apple compacta");
        producto.setPrecio(650000.0);
        producto.setStock(12);

        Producto productoGuardado = productoRepository.save(producto);

        // When & Then
        mockMvc.perform(get("/producto/" + productoGuardado.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("iPad mini"))
                .andExpect(jsonPath("$.precio").value(650000.0));
    }

    @Test
    @DisplayName("Debe crear nuevo producto - API v1")
    void deberiaCrearNuevoProductoV1() throws Exception {
        // Given
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("AirPods 3");
        nuevoProducto.setDescripcion("Auriculares Apple v1");
        nuevoProducto.setPrecio(250000.0);
        nuevoProducto.setStock(25);

        // When & Then
        mockMvc.perform(post("/producto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoProducto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("AirPods 3"))
                .andExpect(jsonPath("$.precio").value(250000.0))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Debe actualizar producto - API v1")
    void deberiaActualizarProductoV1() throws Exception {
        // Given
        Producto producto = new Producto();
        producto.setNombre("Apple Watch SE");
        producto.setDescripcion("Smartwatch Apple básico");
        producto.setPrecio(350000.0);
        producto.setStock(20);

        Producto productoGuardado = productoRepository.save(producto);

        // Producto actualizado
        productoGuardado.setNombre("Apple Watch Series 8");
        productoGuardado.setPrecio(450000.0);

        // When & Then
        mockMvc.perform(put("/producto/" + productoGuardado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoGuardado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Apple Watch Series 8"))
                .andExpect(jsonPath("$.precio").value(450000.0));
    }

    @Test
    @DisplayName("Debe eliminar producto - API v1")
    void deberiaEliminarProductoV1() throws Exception {
        // Given
        Producto producto = new Producto();
        producto.setNombre("iMac");
        producto.setDescripcion("Computadora de escritorio Apple");
        producto.setPrecio(1500000.0);
        producto.setStock(6);

        Producto productoGuardado = productoRepository.save(producto);

        // When & Then
        mockMvc.perform(delete("/producto/" + productoGuardado.getId()))
                .andExpect(status().isOk());

        // Verificar que el producto fue eliminado (debería retornar vacío)
        mockMvc.perform(get("/producto/" + productoGuardado.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Debe manejar producto no encontrado - API v1")
    void deberiaManejarProductoNoEncontradoV1() throws Exception {
        // When & Then
        mockMvc.perform(get("/producto/99999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Debe manejar lista vacía de productos - API v1")
    void deberiaManejarListaVaciaV1() throws Exception {
        // When & Then
        mockMvc.perform(get("/producto"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
