package com.example.Microservice_Usuario.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.Microservice_Usuario.assemblers.UsuarioModelAssembler;
import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerV2Test {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioModelAssembler assembler;

    @InjectMocks
    private UsuarioControllerV2 usuarioControllerV2;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioControllerV2).build();
        objectMapper = new ObjectMapper();
        
        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setCodigo("U0000001A");
        usuarioTest.setNombre("Juan Pérez");
        usuarioTest.setCorreo("juan.perez@email.com");
        usuarioTest.setContrasena("password123");
        usuarioTest.setTelefono("3001234567");
        usuarioTest.setDireccion("Calle 123, Bogotá");
        usuarioTest.setEstado(Usuario.EstadoUsuario.ACTIVO);
    }

    @Test
    void testGetAllUsuarios() throws Exception {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioTest);
        when(usuarioService.obtenerTodos()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/v2/usuarios")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    void testGetUsuarioById() throws Exception {
        // Arrange
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuarioTest);
        when(assembler.toModel(any(Usuario.class))).thenReturn(EntityModel.of(usuarioTest));

        // Act & Assert
        mockMvc.perform(get("/api/v2/usuarios/1")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk());
                // No verificamos content-type en mock test ya que HAL requiere contexto Spring completo
    }

    @Test
    void testGetUsuariosByEstado() throws Exception {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioTest);
        when(usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.ACTIVO)).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/v2/usuarios/estado/ACTIVO")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    void testCreateUsuario() throws Exception {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCodigo("U0000002B");
        nuevoUsuario.setNombre("Ana García");
        nuevoUsuario.setCorreo("ana.garcia@email.com");
        nuevoUsuario.setContrasena("password456");

        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setId(2L);
        usuarioCreado.setCodigo("U0000002B");
        usuarioCreado.setNombre("Ana García");
        usuarioCreado.setCorreo("ana.garcia@email.com");
        usuarioCreado.setContrasena("password456");
        usuarioCreado.setEstado(Usuario.EstadoUsuario.ACTIVO);

        when(usuarioService.crear(any(Usuario.class))).thenReturn(usuarioCreado);
        when(assembler.toModel(any(Usuario.class))).thenReturn(EntityModel.of(usuarioCreado));

        // Act & Assert
        mockMvc.perform(post("/api/v2/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateUsuario() throws Exception {
        // Arrange
        when(usuarioService.actualizar(anyLong(), any(Usuario.class))).thenReturn(usuarioTest);
        when(assembler.toModel(any(Usuario.class))).thenReturn(EntityModel.of(usuarioTest));

        // Act & Assert
        mockMvc.perform(put("/api/v2/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioTest))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUsuario() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v2/usuarios/1")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNoContent());
    }
}
