package com.example.tevp.controller;

import com.example.tevp.model.Descuento;
import com.example.tevp.service.DescuentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/descuentos")
public class DescuentoController {

    private final DescuentoService descuentoService;

    public DescuentoController(DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Descuento>> obtenerDescuentoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(descuentoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Descuento> guardarDescuento(@RequestBody Descuento descuento) {
        return ResponseEntity.ok(descuentoService.guardarDescuento(descuento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDescuento(@PathVariable Long id) {
        descuentoService.eliminarDescuento(id);
        return ResponseEntity.noContent().build();
    }
}