package com.example.tevp.repository;

import com.example.tevp.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Cacheable(value = "productos")
    List<Producto> findByTituloContaining(String titulo);

}
