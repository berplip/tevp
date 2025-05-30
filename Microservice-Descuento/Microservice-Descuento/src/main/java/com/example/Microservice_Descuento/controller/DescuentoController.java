package com.example.Microservice_Descuento.controller;

import com.example.Microservice_Descuento.model.Descuento;
import com.example.Microservice_Descuento.service.DescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descuento")
public class DescuentoController {

    @Autowired
    private DescuentoService descuentoService;

    @GetMapping
    public List<Descuento> obtenerTodos() {
        return descuentoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Descuento obtenerPorId(@PathVariable Long id) {
        return descuentoService.obtenerPorId(id);
    }

    @PostMapping
    public Descuento crear(@RequestBody Descuento descuento) {
        return descuentoService.crear(descuento);
    }

    @PutMapping("/{id}")
    public Descuento actualizar(@PathVariable Long id, @RequestBody Descuento descuento) {
        return descuentoService.actualizar(id, descuento);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        descuentoService.eliminar(id);
    }
}
