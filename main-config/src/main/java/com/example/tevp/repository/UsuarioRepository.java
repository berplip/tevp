package com.example.tevp.repository;

import com.example.tevp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Cacheable(value = "usuarios")
    Usuario findByEmail(String email);

}
