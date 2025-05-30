package com.example.Microservice_Resena.controller;

import com.example.Microservice_Resena.model.Resena;
import com.example.Microservice_Resena.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resena")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    public List<Resena> obtenerTodas() {
        return resenaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Resena obtenerPorId(@PathVariable Long id) {
        return resenaService.obtenerPorId(id);
    }

    @PostMapping
    public Resena crear(@RequestBody Resena resena) {
        return resenaService.crear(resena);
    }

    @PutMapping("/{id}")
    public Resena actualizar(@PathVariable Long id, @RequestBody Resena resena) {
        return resenaService.actualizar(id, resena);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
    }
}
