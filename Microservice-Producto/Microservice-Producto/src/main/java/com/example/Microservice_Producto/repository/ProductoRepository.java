package com.example.Microservice_Producto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Microservice_Producto.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    /**
     * Busca un producto por su código único
     * @param codigo Código único del producto
     * @return Optional<Producto> El producto si existe
     */
    Optional<Producto> findByCodigo(String codigo);
    
    /**
     * Verifica si existe un producto con el código dado
     * @param codigo Código único del producto
     * @return true si existe, false en caso contrario
     */
    boolean existsByCodigo(String codigo);
}
