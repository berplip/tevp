package com.example.Microservice_Usuario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Microservice_Usuario.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuario por correo (útil para login)
    Optional<Usuario> findByCorreo(String correo);
    
    // Buscar usuario por código
    Optional<Usuario> findByCodigo(String codigo);
    
    // Verificar si existe un usuario con ese correo
    boolean existsByCorreo(String correo);
    
    // Verificar si existe un usuario con ese código
    boolean existsByCodigo(String codigo);
    
    // Buscar usuarios por estado
    List<Usuario> findByEstado(Usuario.EstadoUsuario estado);
    
    // Buscar usuarios por nombre (contiene)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    // Contar usuarios por estado
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.estado = :estado")
    long countByEstado(@Param("estado") Usuario.EstadoUsuario estado);
    
    // Buscar usuarios activos
    @Query("SELECT u FROM Usuario u WHERE u.estado = 'ACTIVO'")
    List<Usuario> findAllActiveUsers();
}
