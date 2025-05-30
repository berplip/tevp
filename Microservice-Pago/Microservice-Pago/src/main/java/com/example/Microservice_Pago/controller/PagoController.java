package com.example.Microservice_Pago.controller;

import com.example.Microservice_Pago.model.Pago;
import com.example.Microservice_Pago.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public List<Pago> obtenerTodos() {
        return pagoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Pago obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id);
    }

    @PostMapping
    public Pago crear(@RequestBody Pago pago) {
        return pagoService.crear(pago);
    }

    @PutMapping("/{id}")
    public Pago actualizar(@PathVariable Long id, @RequestBody Pago pago) {
        return pagoService.actualizar(id, pago);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
    }
}
