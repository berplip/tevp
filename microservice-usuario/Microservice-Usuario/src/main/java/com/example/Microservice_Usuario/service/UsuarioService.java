package com.example.Microservice_Usuario.service;

import com.example.Microservice_Usuario.model.PedidoDTO;
import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElse(null);
    }

    public Usuario crear(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public PedidoDTO[] obtenerPedidosPorUsuario(Long usuarioId) {
        String url = "http://localhost:8086/pedido?usuarioId=" + usuarioId;
        return restTemplate.getForObject(url, PedidoDTO[].class);
    }
}
