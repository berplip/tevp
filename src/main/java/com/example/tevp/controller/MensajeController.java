package com.example.tevp.controller;

import com.example.tevp.model.Mensaje;
import com.example.tevp.service.MensajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mensajes")
public class MensajeController {

    private final MensajeService mensajeService;

    public MensajeController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Mensaje>> obtenerMensajePorId(@PathVariable Long id) {
        Optional<Mensaje> mensaje = mensajeService.obtenerPorId(id);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/remitente/{remitenteId}")
    public ResponseEntity<List<Mensaje>> obtenerMensajesPorRemitente(@PathVariable Long remitenteId) {
        List<Mensaje> mensajes = mensajeService.obtenerPorRemitente(remitenteId);
        return ResponseEntity.ok(mensajes);
    }

    @PostMapping
    public ResponseEntity<Mensaje> guardarMensaje(@RequestBody Mensaje mensaje) {
        Mensaje nuevoMensaje = mensajeService.guardarMensaje(mensaje);
        return ResponseEntity.ok(nuevoMensaje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long id) {
        mensajeService.eliminarMensaje(id);
        return ResponseEntity.noContent().build();
    }
}