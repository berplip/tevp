package com.example.tevp.controller;

import com.example.tevp.model.Resena;
import com.example.tevp.service.ResenaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reseñas")
public class ResenaController {

    private final ResenaService reseñaService;

    public ResenaController(ResenaService resenaService) {
        this.reseñaService = resenaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Resena>> obtenerReseñaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reseñaService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Resena>> obtenerReseñasPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(reseñaService.obtenerPorProducto(productoId));
    }

    @PostMapping
    public ResponseEntity<Resena> guardarReseña(@RequestBody Resena reseña) {
        return ResponseEntity.ok(reseñaService.guardarReseña(reseña));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReseña(@PathVariable Long id) {
        reseñaService.eliminarReseña(id);
        return ResponseEntity.noContent().build();
    }
}