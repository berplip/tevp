// src/main/java/com/example/Microservice_Usuario/service/UsuarioService.java
package com.example.Microservice_Usuario.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.Microservice_Usuario.model.PedidoDTO;
import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> obtenerPorEstado(Usuario.EstadoUsuario estado) {
        return usuarioRepository.findByEstado(estado);
    }

    public Usuario obtenerPorId(Long id) {
        // Lanza una excepción si el usuario no se encuentra
        return usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    public Usuario crear(Usuario usuario) {
        // Generar código automáticamente si no se proporciona
        if (usuario.getCodigo() == null || usuario.getCodigo().trim().isEmpty()) {
            usuario.setCodigo(generarCodigoUnico());
        }
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

    // Métodos adicionales para reportes y búsquedas
    public List<Usuario> buscarPorNombreContiene(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con correo: " + correo));
    }

    public Usuario buscarPorCodigo(String codigo) {
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con código: " + codigo));
    }

    public Long contarPorEstado(Usuario.EstadoUsuario estado) {
        return usuarioRepository.countByEstado(estado);
    }

    public Long contarTodos() {
        return usuarioRepository.count();
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

    private String generarCodigoUnico() {
        String codigo;
        Random random = new Random();
        int intentos = 0;
        
        do {
            // Generar código con formato: U + 7 dígitos + 1 letra
            String numero = String.format("%07d", random.nextInt(9999999) + 1);
            String letra = String.valueOf((char) ('A' + random.nextInt(26)));
            codigo = "U" + numero + letra;
            intentos++;
            
            // Evitar bucle infinito
            if (intentos > 100) {
                codigo = "U" + System.currentTimeMillis() + "Z";
                break;
            }
        } while (usuarioRepository.existsByCodigo(codigo));
        
        return codigo;
    }
}