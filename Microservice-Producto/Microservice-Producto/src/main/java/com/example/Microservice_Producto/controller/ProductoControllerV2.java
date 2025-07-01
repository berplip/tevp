package com.example.Microservice_Producto.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Microservice_Producto.assemblers.ProductoModelAssembler;
import com.example.Microservice_Producto.model.Producto;
import com.example.Microservice_Producto.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de productos con soporte HATEOAS (versión 2).
 * Proporciona endpoints RESTful con enlaces de hipermedia.
 */
@RestController
@RequestMapping("/api/v2/productos")
@Tag(name = "Productos V2", description = "API para la gestión de productos con HATEOAS")
public class ProductoControllerV2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los productos", 
               description = "Retorna una colección de productos con enlaces de hipermedia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(mediaType = "application/hal+json"))
    })
    public CollectionModel<EntityModel<Producto>> getAllProductos() {
        List<EntityModel<Producto>> productos = productoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).getAllProductos()).withSelfRel());
    }

    @GetMapping(value = "/{codigo}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener producto por código único", 
               description = "Retorna un producto específico usando su código único para comunicación entre máquinas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/hal+json")),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public EntityModel<Producto> getProductoByCodigo(
            @Parameter(description = "Código único del producto", required = true, example = "P1A2B3C") 
            @PathVariable String codigo) {
        Producto producto = productoService.obtenerPorCodigo(codigo);
        return assembler.toModel(producto);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo producto", 
               description = "Crea un nuevo producto con enlaces de hipermedia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/hal+json")),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    public ResponseEntity<EntityModel<Producto>> createProducto(
            @Parameter(description = "Datos del producto a crear", required = true)
            @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.crear(producto);
        
        return ResponseEntity
                .created(linkTo(methodOn(ProductoControllerV2.class).getProductoByCodigo(nuevoProducto.getCodigo())).toUri())
                .body(assembler.toModel(nuevoProducto));
    }

    @PutMapping(value = "/{codigo}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar producto completo", 
               description = "Actualiza completamente un producto con enlaces de hipermedia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/hal+json")),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    public ResponseEntity<EntityModel<Producto>> updateProducto(
            @Parameter(description = "Código del producto a actualizar", required = true, example = "P1A2B3C")
            @PathVariable String codigo,
            @Parameter(description = "Nuevos datos del producto", required = true)
            @RequestBody Producto producto) {
        producto.setCodigo(codigo);
        Producto productoActualizado = productoService.actualizar(codigo, producto);
        
        return ResponseEntity
                .ok(assembler.toModel(productoActualizado));
    }

    @DeleteMapping(value = "/{codigo}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar producto", 
               description = "Elimina un producto del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public ResponseEntity<?> deleteProducto(
            @Parameter(description = "Código del producto a eliminar", required = true, example = "P1A2B3C")
            @PathVariable String codigo) {
        productoService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}
