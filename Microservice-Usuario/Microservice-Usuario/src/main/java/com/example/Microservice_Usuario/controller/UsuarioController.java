package com.example.Microservice_Usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de usuarios (versión estándar).
 * Proporciona endpoints RESTful para operaciones CRUD y consultas.
 */
@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios - Operaciones estándar")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener todos los usuarios", 
               description = "Retorna una lista de todos los usuarios disponibles en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Usuario.class)))
    })
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public Usuario obtenerPorId(
        @Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.crear(usuario);
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza completamente un usuario existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public Usuario actualizar(
        @Parameter(description = "ID del usuario", required = true) @PathVariable Long id, 
        @RequestBody Usuario usuario) {
        return usuarioService.actualizar(id, usuario);
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener usuarios por estado", description = "Retorna usuarios filtrados por estado")
    @GetMapping("/estado/{estado}")
    public List<Usuario> obtenerPorEstado(
        @Parameter(description = "Estado del usuario", required = true) @PathVariable Usuario.EstadoUsuario estado) {
        return usuarioService.obtenerPorEstado(estado);
    }

    @Operation(summary = "Buscar usuarios por nombre", description = "Busca usuarios que contengan el nombre especificado")
    @GetMapping("/buscar/nombre/{nombre}")
    public List<Usuario> buscarPorNombre(
        @Parameter(description = "Nombre a buscar", required = true) @PathVariable String nombre) {
        return usuarioService.buscarPorNombreContiene(nombre);
    }

    @Operation(summary = "Buscar usuario por correo", description = "Busca un usuario por su correo electrónico")
    @GetMapping("/buscar/correo/{correo}")
    public Usuario buscarPorCorreo(
        @Parameter(description = "Correo electrónico", required = true) @PathVariable String correo) {
        return usuarioService.buscarPorCorreo(correo);
    }

    @Operation(summary = "Buscar usuario por código", description = "Busca un usuario por su código único")
    @GetMapping("/codigo/{codigo}")
    public Usuario buscarPorCodigo(
        @Parameter(description = "Código del usuario", required = true) @PathVariable String codigo) {
        return usuarioService.buscarPorCodigo(codigo);
    }

    // Endpoints de reportes
    @Operation(summary = "Obtener estadísticas de usuarios", description = "Retorna estadísticas generales de usuarios")
    @GetMapping("/reportes/estadisticas")
    public EstadisticasResponse getEstadisticas() {
        long total = usuarioService.contarTodos();
        long activos = usuarioService.contarPorEstado(Usuario.EstadoUsuario.ACTIVO);
        long inactivos = usuarioService.contarPorEstado(Usuario.EstadoUsuario.INACTIVO);
        long bloqueados = usuarioService.contarPorEstado(Usuario.EstadoUsuario.BLOQUEADO);
        
        return new EstadisticasResponse(total, activos, inactivos, bloqueados);
    }

    @Operation(summary = "Contar usuarios por estado", description = "Retorna el número de usuarios en un estado específico")
    @GetMapping("/reportes/conteo/{estado}")
    public ConteoResponse contarPorEstado(
        @Parameter(description = "Estado a contar", required = true) @PathVariable Usuario.EstadoUsuario estado) {
        long conteo = usuarioService.contarPorEstado(estado);
        return new ConteoResponse(estado.name(), conteo);
    }

    @Operation(summary = "Obtener total de usuarios", description = "Retorna el número total de usuarios registrados")
    @GetMapping("/reportes/total")
    public TotalResponse getTotalUsuarios() {
        long total = usuarioService.contarTodos();
        return new TotalResponse(total);
    }

    @Operation(summary = "Obtener usuarios activos recientes", description = "Retorna los primeros 10 usuarios activos")
    @GetMapping("/reportes/activos-recientes")
    public List<Usuario> getUsuariosActivosRecientes() {
        return usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.ACTIVO)
                .stream()
                .limit(10)
                .collect(java.util.stream.Collectors.toList());
    }

    // Clases internas para respuestas de reportes
    public static class EstadisticasResponse {
        public final long total;
        public final long activos;
        public final long inactivos;
        public final long bloqueados;

        public EstadisticasResponse(long total, long activos, long inactivos, long bloqueados) {
            this.total = total;
            this.activos = activos;
            this.inactivos = inactivos;
            this.bloqueados = bloqueados;
        }
    }

    public static class ConteoResponse {
        public final String estado;
        public final long conteo;

        public ConteoResponse(String estado, long conteo) {
            this.estado = estado;
            this.conteo = conteo;
        }
    }

    public static class TotalResponse {
        public final long total;

        public TotalResponse(long total) {
            this.total = total;
        }
    }
}
