package com.example.Microservice_Notificacion.controller;

import com.example.Microservice_Notificacion.model.Notificacion;
import com.example.Microservice_Notificacion.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacion")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public List<Notificacion> obtenerTodas() {
        return notificacionService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Notificacion obtenerPorId(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id);
    }

    @PostMapping
    public Notificacion crear(@RequestBody Notificacion notificacion) {
        return notificacionService.crear(notificacion);
    }

    @PutMapping("/{id}")
    public Notificacion actualizar(@PathVariable Long id, @RequestBody Notificacion notificacion) {
        return notificacionService.actualizar(id, notificacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
    }
}
