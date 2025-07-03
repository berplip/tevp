package com.example.Microservice_Usuario.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Microservice_Usuario.assemblers.UsuarioModelAssembler;
import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de usuarios (versión avanzada).
 * Proporciona endpoints RESTful con navegación de enlaces.
 */
@RestController
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Usuarios V2", description = "API para la gestión de usuarios - Operaciones avanzadas")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los usuarios", 
               description = "Retorna una colección de usuarios con enlaces de navegación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(mediaType = "application/hal+json"))
    })
    public CollectionModel<EntityModel<Usuario>> getAllUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(usuarios)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(Usuario.EstadoUsuario.ACTIVO)).withRel("usuarios-activos"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(Usuario.EstadoUsuario.INACTIVO)).withRel("usuarios-inactivos"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(Usuario.EstadoUsuario.BLOQUEADO)).withRel("usuarios-bloqueados"));
    }

    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico con enlaces de navegación")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return assembler.toModel(usuario);
    }

    @Operation(summary = "Obtener usuario por código", description = "Retorna un usuario específico por código con enlaces de navegación")
    @GetMapping(value = "/codigo/{codigo}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Usuario> getUsuarioByCodigo(@PathVariable String codigo) {
        Usuario usuario = usuarioService.buscarPorCodigo(codigo);
        return assembler.toModel(usuario);
    }

    @Operation(summary = "Obtener usuarios por estado", description = "Retorna usuarios filtrados por estado con enlaces de navegación")
    @GetMapping(value = "/estado/{estado}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Usuario>> getUsuariosByEstado(@PathVariable Usuario.EstadoUsuario estado) {
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerPorEstado(estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(usuarios)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(estado)).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("todos-usuarios"));
    }

    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario con enlaces de navegación")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> createUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.crear(usuario);
        
        return ResponseEntity
                .created(linkTo(methodOn(UsuarioControllerV2.class).getUsuarioByCodigo(nuevoUsuario.getCodigo())).toUri())
                .body(assembler.toModel(nuevoUsuario));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario existente con enlaces de navegación")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizar(id, usuario);
        return assembler.toModel(usuarioActualizado);
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints adicionales específicos para reportes personalizados
    @GetMapping(value = "/reportes/estadisticas", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<String>>> getEstadisticasUsuarios() {
        // Crear estadísticas como texto
        String estadisticas = String.format("Total usuarios: %d, Activos: %d, Inactivos: %d, Bloqueados: %d",
                usuarioService.obtenerTodos().size(),
                usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.ACTIVO).size(),
                usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.INACTIVO).size(),
                usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.BLOQUEADO).size());
        
        EntityModel<String> estadisticasModel = EntityModel.of(estadisticas)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getEstadisticasUsuarios()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("todos-usuarios"));
        
        return ResponseEntity.ok(
            CollectionModel.of(List.of(estadisticasModel))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getEstadisticasUsuarios()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("todos-usuarios"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(Usuario.EstadoUsuario.ACTIVO)).withRel("usuarios-activos"))
        );
    }

    @GetMapping(value = "/reportes/resumen", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<String>>> getResumenPersonalizado() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        
        // Crear un resumen personalizado como texto
        String resumen = String.format("Resumen generado el %s - Total usuarios: %d - Distribución por estado: Activos: %d, Inactivos: %d, Bloqueados: %d",
                java.time.LocalDateTime.now().toString(),
                usuarios.size(),
                usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.ACTIVO).size(),
                usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.INACTIVO).size(),
                usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.BLOQUEADO).size());
        
        EntityModel<String> resumenModel = EntityModel.of(resumen)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getResumenPersonalizado()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getEstadisticasUsuarios()).withRel("estadisticas"));
        
        return ResponseEntity.ok(
            CollectionModel.of(List.of(resumenModel))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getResumenPersonalizado()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getEstadisticasUsuarios()).withRel("estadisticas"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("todos-usuarios"))
        );
    }

    // Reportes personalizados adicionales siguiendo la guía proporcionada
    
    @GetMapping(value = "/reportes/usuarios-por-nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Usuario>> getUsuariosPorNombre(@PathVariable String nombre) {
        List<EntityModel<Usuario>> usuarios = usuarioService.buscarPorNombreContiene(nombre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(usuarios)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosPorNombre(nombre)).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("todos-usuarios"));
    }

    @GetMapping(value = "/reportes/usuarios-por-correo/{correo}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Usuario> getUsuarioPorCorreo(@PathVariable String correo) {
        Usuario usuario = usuarioService.buscarPorCorreo(correo);
        return assembler.toModel(usuario);
    }

    @GetMapping(value = "/reportes/usuarios-por-codigo/{codigo}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Usuario> getUsuarioPorCodigoReporte(@PathVariable String codigo) {
        Usuario usuario = usuarioService.buscarPorCodigo(codigo);
        return assembler.toModel(usuario);
    }

    @GetMapping(value = "/reportes/conteo-por-estado/{estado}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Long>> getConteoPorEstado(@PathVariable Usuario.EstadoUsuario estado) {
        Long conteo = usuarioService.contarPorEstado(estado);
        
        EntityModel<Long> conteoModel = EntityModel.of(conteo)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getConteoPorEstado(estado)).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(estado)).withRel("usuarios-" + estado.name().toLowerCase()));
        
        return ResponseEntity.ok(conteoModel);
    }

    @GetMapping(value = "/reportes/usuarios-activos-recientes", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Usuario>> getUsuariosActivosRecientes() {
        // Para este ejemplo, filtramos solo usuarios activos
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.ACTIVO).stream()
                .limit(10) // Limitar a los primeros 10
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(usuarios)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosActivosRecientes()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("todos-usuarios"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(Usuario.EstadoUsuario.ACTIVO)).withRel("todos-activos"));
    }

    @GetMapping(value = "/reportes/total-usuarios", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Long>> getTotalUsuarios() {
        Long total = usuarioService.contarTodos();
        
        EntityModel<Long> totalModel = EntityModel.of(total)
                .add(linkTo(methodOn(UsuarioControllerV2.class).getTotalUsuarios()).withSelfRel())
                .add(linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("todos-usuarios"))
                .add(linkTo(methodOn(UsuarioControllerV2.class).getEstadisticasUsuarios()).withRel("estadisticas"));
        
        return ResponseEntity.ok(totalModel);
    }
}
