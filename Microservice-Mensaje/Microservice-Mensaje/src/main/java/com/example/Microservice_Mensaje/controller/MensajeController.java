package com.example.Microservice_Mensaje.controller;

import com.example.Microservice_Mensaje.model.Mensaje;
import com.example.Microservice_Mensaje.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensaje")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @GetMapping
    public List<Mensaje> obtenerTodos() {
        return mensajeService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Mensaje obtenerPorId(@PathVariable Long id) {
        return mensajeService.obtenerPorId(id);
    }

    @PostMapping
    public Mensaje crear(@RequestBody Mensaje mensaje) {
        return mensajeService.crear(mensaje);
    }

    @PutMapping("/{id}")
    public Mensaje actualizar(@PathVariable Long id, @RequestBody Mensaje mensaje) {
        return mensajeService.actualizar(id, mensaje);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        mensajeService.eliminar(id);
    }
}
