package com.example.tevp.service;

import com.example.tevp.model.Pedido;
import com.example.tevp.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository PedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.PedidoRepository = pedidoRepository;
    }

    public List<Pedido> obtenerPorEstado(String estado) {
        return PedidoRepository.findByEstado(estado);
    }

    public List<Pedido> obtenerTodos() {
        return PedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return PedidoRepository.findById(id);
    }

    public Pedido guardarPedido(Pedido pedido) {
        return PedidoRepository.save(pedido);

    }

}
