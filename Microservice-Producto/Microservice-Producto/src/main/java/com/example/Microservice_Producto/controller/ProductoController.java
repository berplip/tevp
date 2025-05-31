package com.example.Microservice_Producto.controller;

import com.example.Microservice_Producto.model.Producto;
import com.example.Microservice_Producto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // GET: Obtener todos los productos
    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    // GET: Obtener producto por ID
    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    // POST: Crear un nuevo producto
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoService.crear(producto);
    }

    // PUT: Actualizar un producto completamente
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizar(id, producto);
    }

    // PATCH: Actualizar parcialmente un producto
    @PatchMapping("/{id}")
    public Producto actualizarParcialmente(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
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
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }
}
