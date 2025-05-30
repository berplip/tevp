package com.example.Microservice_SoporteOnline.controller;

import com.example.Microservice_SoporteOnline.model.SoporteOnline;
import com.example.Microservice_SoporteOnline.service.SoporteOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/soporteonline")
public class SoporteOnlineController {

    @Autowired
    private SoporteOnlineService soporteOnlineService;

    @GetMapping
    public List<SoporteOnline> obtenerTodos() {
        return soporteOnlineService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public SoporteOnline obtenerPorId(@PathVariable Long id) {
        return soporteOnlineService.obtenerPorId(id);
    }

    @PostMapping
    public SoporteOnline crear(@RequestBody SoporteOnline soporteOnline) {
        return soporteOnlineService.crear(soporteOnline);
    }

    @PutMapping("/{id}")
    public SoporteOnline actualizar(@PathVariable Long id, @RequestBody SoporteOnline soporteOnline) {
        return soporteOnlineService.actualizar(id, soporteOnline);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        soporteOnlineService.eliminar(id);
    }
}
