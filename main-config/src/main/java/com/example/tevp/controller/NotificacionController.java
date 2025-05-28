package com.example.tevp.controller;

import com.example.tevp.model.Notificacion;
import com.example.tevp.service.NotificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Notificacion> guardarNotificacion(@RequestBody Notificacion notificacion) {
        return ResponseEntity.ok(notificacionService.guardarNotificacion(notificacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}