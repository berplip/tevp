package com.example.Microservice_Usuario.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombre("Juan Pérez");
        usuarioTest.setCorreo("juan.perez@email.com");
        usuarioTest.setContrasena("password123");
        usuarioTest.setTelefono("3001234567");
        usuarioTest.setDireccion("Calle 123, Bogotá");
        usuarioTest.setEstado(Usuario.EstadoUsuario.ACTIVO);
    }

    @Test
    void testObtenerTodos() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioTest);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> resultado = usuarioService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_UsuarioExiste() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));

        // Act
        Usuario resultado = usuarioService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_UsuarioNoExiste() {
        // Arrange
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> usuarioService.obtenerPorId(1L));
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Usuario no encontrado con ID: 1"));
    }

    @Test
    void testObtenerPorEstado() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioTest);
        when(usuarioRepository.findByEstado(Usuario.EstadoUsuario.ACTIVO)).thenReturn(usuarios);

        // Act
        List<Usuario> resultado = usuarioService.obtenerPorEstado(Usuario.EstadoUsuario.ACTIVO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(Usuario.EstadoUsuario.ACTIVO, resultado.get(0).getEstado());
        verify(usuarioRepository, times(1)).findByEstado(Usuario.EstadoUsuario.ACTIVO);
    }

    @Test
    void testCrear() {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Ana García");
        nuevoUsuario.setCorreo("ana.garcia@email.com");
        nuevoUsuario.setContrasena("password456");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        // Act
        Usuario resultado = usuarioService.crear(nuevoUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre()); // Retorna el mock
        verify(usuarioRepository, times(1)).save(nuevoUsuario);
    }

    @Test
    void testActualizar_UsuarioExiste() {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Juan Carlos Pérez");
        usuarioActualizado.setCorreo("juan.carlos@email.com");

        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act
        Usuario resultado = usuarioService.actualizar(1L, usuarioActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Carlos Pérez", resultado.getNombre());
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).save(usuarioActualizado);
    }

    @Test
    void testActualizar_UsuarioNoExiste() {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        when(usuarioRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> usuarioService.actualizar(1L, usuarioActualizado));
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testEliminar_UsuarioExiste() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        // Act
        usuarioService.eliminar(1L);

        // Assert
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_UsuarioNoExiste() {
        // Arrange
        when(usuarioRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> usuarioService.eliminar(1L));
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}
