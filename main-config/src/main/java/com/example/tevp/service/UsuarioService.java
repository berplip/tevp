package com.example.tevp.service;

import com.example.tevp.model.Usuario;
import com.example.tevp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Cacheable("usuarios")
    public Optional<Usuario> obtenerPorEmail(String email) {
        return Optional.ofNullable(usuarioRepository.findByEmail(email));
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}