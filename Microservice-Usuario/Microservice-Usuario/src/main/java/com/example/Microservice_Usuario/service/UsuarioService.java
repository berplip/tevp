// src/main/java/com/example/Microservice_Usuario/service/UsuarioService.java
package com.example.Microservice_Usuario.service;

import com.example.Microservice_Usuario.model.PedidoDTO;
import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        // Lanza una excepción si el usuario no se encuentra
        return usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    public Usuario crear(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        if (!usuarioRepository.existsById(id)) {
            // Lanza una excepción si el usuario a actualizar no se encuentra
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id);
        }
        usuario.setId(id); // Asegura que el ID del usuario a actualizar sea el correcto
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            // Lanza una excepción si el usuario a eliminar no se encuentra
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public PedidoDTO[] obtenerPedidosPorUsuario(Long usuarioId) {
        String url = "http://localhost:8086/pedido?usuarioId=" + usuarioId;
        // Podrías añadir manejo de errores aquí si el servicio de pedidos no está
        // disponible
        try {
            return restTemplate.getForObject(url, PedidoDTO[].class);
        } catch (Exception e) {
            // Log the exception and potentially return an empty array or throw a custom
            // exception
            System.err.println("Error al obtener pedidos para el usuario " + usuarioId + ": " + e.getMessage());
            return new PedidoDTO[] {}; // Devuelve un array vacío en caso de error
        }
    }
}