package com.example.Microservice_Producto.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Microservice_Producto.model.Producto;
import com.example.Microservice_Producto.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/producto")
@Tag(name = "Productos", description = "API para la gestión de productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // GET: Obtener todos los productos
    @GetMapping
    @Operation(summary = "Obtener todos los productos", 
               description = "Retorna una lista de todos los productos disponibles en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class)))
    })
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    // GET: Obtener producto por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", 
               description = "Retorna un producto específico basado en su ID único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public Producto obtenerPorId(@Parameter(description = "ID único del producto", required = true, example = "1") 
                                @PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    // POST: Crear un nuevo producto
    @PostMapping
    @Operation(summary = "Crear un nuevo producto", 
               description = "Crea un nuevo producto en el sistema con la información proporcionada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    public Producto crear(@Parameter(description = "Datos del producto a crear", required = true)
                         @RequestBody Producto producto) {
        return productoService.crear(producto);
    }

    // PUT: Actualizar un producto completamente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto completo", 
               description = "Actualiza completamente un producto existente con nuevos datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    public Producto actualizar(@Parameter(description = "ID del producto a actualizar", required = true, example = "1")
                              @PathVariable Long id, 
                              @Parameter(description = "Nuevos datos del producto", required = true)
                              @RequestBody Producto producto) {
        return productoService.actualizar(id, producto);
    }

    // PATCH: Actualizar parcialmente un producto
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar producto parcialmente", 
               description = "Actualiza solo los campos especificados de un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado parcialmente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    public Producto actualizarParcialmente(@Parameter(description = "ID del producto a actualizar", required = true, example = "1")
                                          @PathVariable Long id, 
                                          @Parameter(description = "Campos a actualizar del producto", required = true)
                                          @RequestBody Map<String, Object> campos) {
        Producto producto = productoService.obtenerPorId(id);
        campos.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Producto.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, producto, value);
            }
        });
        return productoService.actualizar(id, producto);
    }

    // DELETE: Eliminar un producto
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", 
               description = "Elimina un producto del sistema de forma permanente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public void eliminar(@Parameter(description = "ID del producto a eliminar", required = true, example = "1")
                        @PathVariable Long id) {
        productoService.eliminar(id);
    }
}
