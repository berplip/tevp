package com.example.tevp.controller;

import com.example.tevp.model.Producto;
import com.example.tevp.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoService.obtenerPorId(id);
        return producto.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/{titulo}")
    public ResponseEntity<List<Producto>> obtenerProductosPorTitulo(@PathVariable String titulo) {
        List<Producto> productos = productoService.obtenerPorTitulo(titulo);
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<Producto> guardarProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardarProducto(producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}