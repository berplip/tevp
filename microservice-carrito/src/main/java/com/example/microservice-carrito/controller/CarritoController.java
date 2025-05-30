package com.example.tevp.controller;

// w

import com.example.tevp.model.Carrito;
import com.example.tevp.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Carrito>> obtenerCarritoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Carrito> guardarCarrito(@RequestBody Carrito carrito) {
        return ResponseEntity.ok(carritoService.guardarCarrito(carrito));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarrito(@PathVariable Long id) {
        carritoService.eliminarCarrito(id);
        return ResponseEntity.noContent().build();
    }
}