package com.example.tevp.controller;

import com.example.tevp.model.SoporteOnline;
import com.example.tevp.service.SoporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/soporte")
public class SoporteController {

    private final SoporteService soporteService;

    public SoporteController(SoporteService soporteService) {
        this.soporteService = soporteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<SoporteOnline>> obtenerSoportePorId(@PathVariable Long id) {
        return ResponseEntity.ok(soporteService.obtenerPorId(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SoporteOnline>> obtenerSoportePorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(soporteService.obtenerPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<SoporteOnline> guardarSoporte(@RequestBody SoporteOnline soporte) {
        return ResponseEntity.ok(soporteService.guardarSoporte(soporte));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSoporte(@PathVariable Long id) {
        soporteService.eliminarSoporte(id);
        return ResponseEntity.noContent().build();
    }
}