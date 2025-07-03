package com.example.Microservice_Usuario.model;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UsuarioTest {

    private Validator validator;
    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        usuarioValido = new Usuario();
        usuarioValido.setCodigo("U0000001A");
        usuarioValido.setNombre("Juan Pérez");
        usuarioValido.setCorreo("juan.perez@email.com");
        usuarioValido.setContrasena("password123");
        usuarioValido.setTelefono("3001234567");
        usuarioValido.setDireccion("Calle 123, Bogotá");
        usuarioValido.setEstado(Usuario.EstadoUsuario.ACTIVO);
    }

    @Test
    void testUsuarioValido() {
        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioValido);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNombreVacio() {
        // Arrange
        usuarioValido.setNombre("");

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioValido);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("El nombre es obligatorio")));
    }

    @Test
    void testNombreNulo() {
        // Arrange
        usuarioValido.setNombre(null);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioValido);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("El nombre es obligatorio")));
    }

    @Test
    void testNombreMuyLargo() {
        // Arrange
        String nombreLargo = "a".repeat(101); // 101 caracteres
        usuarioValido.setNombre(nombreLargo);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioValido);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("El nombre debe tener entre 2 y 100 caracteres")));
    }

    @Test
    void testNombreMuyCorto() {
        // Arrange
        usuarioValido.setNombre("a"); // 1 caracter

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioValido);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("El nombre debe tener entre 2 y 100 caracteres")));
    }

    @Test
    void testCorreoVacio() {
        // Arrange
        usuarioValido.setCorreo("");

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioValido);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("El correo es obligatorio")));
    }

    @Test
    void testCorreoFormatoInvalido() {
        // Arrange
        usuarioValido.setCorreo("correo-invalido");

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioValido);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("El formato del correo no es válido")));
    }

    @Test
    void testContrasenaVacia() {
        // Arrange
        usuarioValido.setContrasena("");

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioValido);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("La contraseña es obligatoria")));
    }

    @Test
    void testContrasenaMuyCorta() {
        // Arrange
        usuarioValido.setContrasena("12345"); // 5 caracteres

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioValido);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("La contraseña debe tener al menos 6 caracteres")));
    }

    @Test
    void testEstadosPorDefecto() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setCorreo("test@email.com");
        usuario.setContrasena("password123");

        // Assert
        assertEquals(Usuario.EstadoUsuario.ACTIVO, usuario.getEstado());
    }

    @Test
    void testTodosLosEstados() {
        // Arrange & Act & Assert
        Usuario usuario = new Usuario();
        
        usuario.setEstado(Usuario.EstadoUsuario.ACTIVO);
        assertEquals(Usuario.EstadoUsuario.ACTIVO, usuario.getEstado());
        
        usuario.setEstado(Usuario.EstadoUsuario.INACTIVO);
        assertEquals(Usuario.EstadoUsuario.INACTIVO, usuario.getEstado());
        
        usuario.setEstado(Usuario.EstadoUsuario.BLOQUEADO);
        assertEquals(Usuario.EstadoUsuario.BLOQUEADO, usuario.getEstado());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Usuario usuario = new Usuario();
        
        // Act & Assert
        usuario.setId(100L);
        assertEquals(100L, usuario.getId());
        
        usuario.setCodigo("U0000100Z");
        assertEquals("U0000100Z", usuario.getCodigo());
        
        usuario.setNombre("Test Usuario");
        assertEquals("Test Usuario", usuario.getNombre());
        
        usuario.setCorreo("test@email.com");
        assertEquals("test@email.com", usuario.getCorreo());
        
        usuario.setContrasena("testpass");
        assertEquals("testpass", usuario.getContrasena());
        
        usuario.setTelefono("3009876543");
        assertEquals("3009876543", usuario.getTelefono());
        
        usuario.setDireccion("Dirección de prueba");
        assertEquals("Dirección de prueba", usuario.getDireccion());
        
        usuario.setEstado(Usuario.EstadoUsuario.INACTIVO);
        assertEquals(Usuario.EstadoUsuario.INACTIVO, usuario.getEstado());
    }

    @Test
    void testConstructorConParametros() {
        // Act
        Usuario usuario = new Usuario(1L, "U0000001A", "Juan Pérez", "juan@gmail.com", "password123", 
                                     "3001234567", "Calle 123", Usuario.EstadoUsuario.ACTIVO);

        // Assert
        assertEquals(1L, usuario.getId());
        assertEquals("U0000001A", usuario.getCodigo());
        assertEquals("Juan Pérez", usuario.getNombre());
        assertEquals("juan@gmail.com", usuario.getCorreo());
        assertEquals("password123", usuario.getContrasena());
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("Calle 123", usuario.getDireccion());
        assertEquals(Usuario.EstadoUsuario.ACTIVO, usuario.getEstado());
    }

    @Test
    void testConstructorSinParametros() {
        // Act
        Usuario usuario = new Usuario();

        // Assert
        assertNull(usuario.getId());
        assertNull(usuario.getCodigo());
        assertNull(usuario.getNombre());
        assertNull(usuario.getCorreo());
        assertNull(usuario.getContrasena());
        assertNull(usuario.getTelefono());
        assertNull(usuario.getDireccion());
        assertEquals(Usuario.EstadoUsuario.ACTIVO, usuario.getEstado()); // Por defecto
    }
}
