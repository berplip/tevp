package com.example.Microservice_Usuario.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.model.Usuario.EstadoUsuario;
import com.example.Microservice_Usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testListarTodos() throws Exception {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setCodigo("USR001");
        usuario1.setNombre("Juan Pérez");
        usuario1.setCorreo("juan@example.com");
        usuario1.setContrasena("password123");
        usuario1.setEstado(EstadoUsuario.ACTIVO);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setCodigo("USR002");
        usuario2.setNombre("María García");
        usuario2.setCorreo("maria@example.com");
        usuario2.setContrasena("password456");
        usuario2.setEstado(EstadoUsuario.ACTIVO);

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioService.obtenerTodos()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/usuario"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("María García"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCodigo("USR001");
        usuario.setNombre("Juan Pérez");
        usuario.setCorreo("juan@example.com");
        usuario.setContrasena("password123");
        usuario.setEstado(EstadoUsuario.ACTIVO);

        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(get("/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.codigo").value("USR001"));
    }

    @Test
    void testCrear() throws Exception {
        // Arrange
        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setId(1L);
        usuarioCreado.setCodigo("USR001");
        usuarioCreado.setNombre("Juan Pérez");
        usuarioCreado.setCorreo("juan@example.com");
        usuarioCreado.setContrasena("password123");
        usuarioCreado.setEstado(EstadoUsuario.ACTIVO);

        when(usuarioService.crear(any(Usuario.class))).thenReturn(usuarioCreado);

        Usuario usuarioInput = new Usuario();
        usuarioInput.setNombre("Juan Pérez");
        usuarioInput.setCorreo("juan@example.com");
        usuarioInput.setContrasena("password123");

        // Act & Assert
        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInput)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    void testActualizar() throws Exception {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setCodigo("USR001");
        usuarioActualizado.setNombre("Juan Pérez Actualizado");
        usuarioActualizado.setCorreo("juan@example.com");
        usuarioActualizado.setContrasena("password123");
        usuarioActualizado.setEstado(EstadoUsuario.ACTIVO);

        when(usuarioService.actualizar(anyLong(), any(Usuario.class))).thenReturn(usuarioActualizado);

        Usuario usuarioInput = new Usuario();
        usuarioInput.setNombre("Juan Pérez Actualizado");
        usuarioInput.setCorreo("juan@example.com");
        usuarioInput.setContrasena("password123");

        // Act & Assert
        mockMvc.perform(put("/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInput)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez Actualizado"));
    }

    @Test
    void testEliminar() throws Exception {
        // Arrange
        doNothing().when(usuarioService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/usuario/1"))
                .andExpect(status().isNoContent()); // 204 es correcto para DELETE
    }
}
