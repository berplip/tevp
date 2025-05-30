package com.example.Microservice_Carrito.controller;

import com.example.Microservice_Carrito.model.Carrito;
import com.example.Microservice_Carrito.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public List<Carrito> obtenerTodos() {
        return carritoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Carrito obtenerPorId(@PathVariable Long id) {
        return carritoService.obtenerPorId(id);
    }

    @PostMapping
    public Carrito crear(@RequestBody Carrito carrito) {
        return carritoService.crear(carrito);
    }

    @PutMapping("/{id}")
    public Carrito actualizar(@PathVariable Long id, @RequestBody Carrito carrito) {
        return carritoService.actualizar(id, carrito);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        carritoService.eliminar(id);
    }
}
