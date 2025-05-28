package com.example.tevp.controller;

import com.example.tevp.model.Pedido;
import com.example.tevp.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Pedido>> obtenerPedidoPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.obtenerPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorEstado(@PathVariable String estado) {
        List<Pedido> pedidos = pedidoService.obtenerPorEstado(estado);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    public ResponseEntity<Pedido> guardarPedido(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.guardarPedido(pedido);
        return ResponseEntity.ok(nuevoPedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}