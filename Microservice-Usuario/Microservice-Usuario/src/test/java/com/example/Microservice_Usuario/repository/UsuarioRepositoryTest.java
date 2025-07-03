package com.example.Microservice_Usuario.repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.Microservice_Usuario.model.Usuario;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        usuarioTest = new Usuario();
        usuarioTest.setNombre("Juan Pérez");
        usuarioTest.setCorreo("juan.perez@test.com");
        usuarioTest.setContrasena("password123");
        usuarioTest.setTelefono("3001234567");
        usuarioTest.setDireccion("Calle 123, Bogotá");
        usuarioTest.setEstado(Usuario.EstadoUsuario.ACTIVO);
    }

    @Test
    void testFindByCorreo_UsuarioExiste() {
        // Arrange
        entityManager.persistAndFlush(usuarioTest);

        // Act
        Optional<Usuario> resultado = usuarioRepository.findByCorreo("juan.perez@test.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
        assertEquals("juan.perez@test.com", resultado.get().getCorreo());
    }

    @Test
    void testFindByCorreo_UsuarioNoExiste() {
        // Act
        Optional<Usuario> resultado = usuarioRepository.findByCorreo("noexiste@test.com");

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void testExistsByCorreo_UsuarioExiste() {
        // Arrange
        entityManager.persistAndFlush(usuarioTest);

        // Act
        boolean existe = usuarioRepository.existsByCorreo("juan.perez@test.com");

        // Assert
        assertTrue(existe);
    }

    @Test
    void testExistsByCorreo_UsuarioNoExiste() {
        // Act
        boolean existe = usuarioRepository.existsByCorreo("noexiste@test.com");

        // Assert
        assertFalse(existe);
    }

    @Test
    void testFindByEstado() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Usuario Activo");
        usuario1.setCorreo("activo@test.com");
        usuario1.setContrasena("password123");
        usuario1.setEstado(Usuario.EstadoUsuario.ACTIVO);

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Usuario Inactivo");
        usuario2.setCorreo("inactivo@test.com");
        usuario2.setContrasena("password123");
        usuario2.setEstado(Usuario.EstadoUsuario.INACTIVO);

        entityManager.persistAndFlush(usuario1);
        entityManager.persistAndFlush(usuario2);

        // Act
        List<Usuario> usuariosActivos = usuarioRepository.findByEstado(Usuario.EstadoUsuario.ACTIVO);
        List<Usuario> usuariosInactivos = usuarioRepository.findByEstado(Usuario.EstadoUsuario.INACTIVO);

        // Assert
        assertEquals(1, usuariosActivos.size());
        assertEquals("Usuario Activo", usuariosActivos.get(0).getNombre());
        
        assertEquals(1, usuariosInactivos.size());
        assertEquals("Usuario Inactivo", usuariosInactivos.get(0).getNombre());
    }

    @Test
    void testFindByNombreContainingIgnoreCase() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Juan Carlos Pérez");
        usuario1.setCorreo("juan.carlos@test.com");
        usuario1.setContrasena("password123");

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Ana María García");
        usuario2.setCorreo("ana.maria@test.com");
        usuario2.setContrasena("password123");

        entityManager.persistAndFlush(usuario1);
        entityManager.persistAndFlush(usuario2);

        // Act
        List<Usuario> resultadoBusqueda = usuarioRepository.findByNombreContainingIgnoreCase("juan");

        // Assert
        assertEquals(1, resultadoBusqueda.size());
        assertEquals("Juan Carlos Pérez", resultadoBusqueda.get(0).getNombre());
    }

    @Test
    void testCountByEstado() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Usuario Activo 1");
        usuario1.setCorreo("activo1@test.com");
        usuario1.setContrasena("password123");
        usuario1.setEstado(Usuario.EstadoUsuario.ACTIVO);

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Usuario Activo 2");
        usuario2.setCorreo("activo2@test.com");
        usuario2.setContrasena("password123");
        usuario2.setEstado(Usuario.EstadoUsuario.ACTIVO);

        Usuario usuario3 = new Usuario();
        usuario3.setNombre("Usuario Bloqueado");
        usuario3.setCorreo("bloqueado@test.com");
        usuario3.setContrasena("password123");
        usuario3.setEstado(Usuario.EstadoUsuario.BLOQUEADO);

        entityManager.persistAndFlush(usuario1);
        entityManager.persistAndFlush(usuario2);
        entityManager.persistAndFlush(usuario3);

        // Act
        long countActivos = usuarioRepository.countByEstado(Usuario.EstadoUsuario.ACTIVO);
        long countBloqueados = usuarioRepository.countByEstado(Usuario.EstadoUsuario.BLOQUEADO);

        // Assert
        assertEquals(2, countActivos);
        assertEquals(1, countBloqueados);
    }

    @Test
    void testFindAllActiveUsers() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Usuario Activo 1");
        usuario1.setCorreo("activo1@test.com");
        usuario1.setContrasena("password123");
        usuario1.setEstado(Usuario.EstadoUsuario.ACTIVO);

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Usuario Inactivo");
        usuario2.setCorreo("inactivo@test.com");
        usuario2.setContrasena("password123");
        usuario2.setEstado(Usuario.EstadoUsuario.INACTIVO);

        entityManager.persistAndFlush(usuario1);
        entityManager.persistAndFlush(usuario2);

        // Act
        List<Usuario> usuariosActivos = usuarioRepository.findAllActiveUsers();

        // Assert
        assertEquals(1, usuariosActivos.size());
        assertEquals("Usuario Activo 1", usuariosActivos.get(0).getNombre());
        assertEquals(Usuario.EstadoUsuario.ACTIVO, usuariosActivos.get(0).getEstado());
    }
}
