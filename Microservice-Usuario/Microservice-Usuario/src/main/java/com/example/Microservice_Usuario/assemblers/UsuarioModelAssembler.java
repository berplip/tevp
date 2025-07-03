package com.example.Microservice_Usuario.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.Microservice_Usuario.controller.UsuarioControllerV2;
import com.example.Microservice_Usuario.model.Usuario;

/**
 * Ensamblador de recursos para el modelo Usuario.
 * Se encarga de crear enlaces HATEOAS para una entidad Usuario.
 */
@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    @NonNull
    public EntityModel<Usuario> toModel(@NonNull Usuario usuario) {
        // Usar ID si el código es null o vacío, sino usar código (pero validar caracteres)
        if (usuario.getCodigo() != null && !usuario.getCodigo().trim().isEmpty()) {
            // Escapar caracteres especiales en el código para URL
            String codigoSeguro = usuario.getCodigo().replaceAll("[{}]", "");
            try {
                return EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioControllerV2.class).getUsuarioByCodigo(codigoSeguro)).withSelfRel(),
                        linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("usuarios"),
                        linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(usuario.getEstado())).withRel("usuarios-por-estado"));
            } catch (Exception e) {
                // Si hay error con el código, usar ID como fallback
                return EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioControllerV2.class).getUsuarioById(usuario.getId())).withSelfRel(),
                        linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("usuarios"),
                        linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(usuario.getEstado())).withRel("usuarios-por-estado"));
            }
        } else {
            return EntityModel.of(usuario,
                    linkTo(methodOn(UsuarioControllerV2.class).getUsuarioById(usuario.getId())).withSelfRel(),
                    linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("usuarios"),
                    linkTo(methodOn(UsuarioControllerV2.class).getUsuariosByEstado(usuario.getEstado())).withRel("usuarios-por-estado"));
        }
    }
}
