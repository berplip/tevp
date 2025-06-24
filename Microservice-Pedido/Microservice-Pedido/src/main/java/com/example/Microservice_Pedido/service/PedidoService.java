package com.example.Microservice_Pedido.service;

import com.example.Microservice_Pedido.model.Pedido;
import com.example.Microservice_Pedido.model.UsuarioDTO;
import com.example.Microservice_Pedido.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.orElse(null);
    }

    public Pedido crear(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizar(Long id, Pedido pedido) {
        if (pedidoRepository.existsById(id)) {
            pedido.setId(id);
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }

    public List<Pedido> obtenerPorUsuarioId(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    // Ejemplo de comunicación con Microservice-Usuario
    public UsuarioDTO obtenerUsuarioPorId(Long usuarioId) {
        String url = "http://localhost:8091/usuario/" + usuarioId;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }
}