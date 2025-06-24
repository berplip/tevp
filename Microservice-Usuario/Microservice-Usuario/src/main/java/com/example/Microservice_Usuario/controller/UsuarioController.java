package com.example.Microservice_Usuario.controller;

import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.ReflectionUtils;

import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET: Obtener todos los usuarios
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    // GET: Obtener usuario por ID
    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    // POST: Crear un nuevo usuario
    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.crear(usuario);
    }

    // PUT: Actualizar un usuario completamente
    @PutMapping("/{id}") //Modifica / Actualiza , mapea peticiones de http
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.actualizar(id, usuario);
    }

    // PATCH: Actualizar parcialmente un usuario
    @PatchMapping("/{id}")
    public Usuario actualizarParcialmente(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        campos.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Usuario.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, usuario, value);
            }
        });
        return usuarioService.actualizar(id, usuario);
    }

    // DELETE: Eliminar un usuario
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}
