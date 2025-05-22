package com.example.tevp.service;

import com.example.tevp.model.Producto;
import com.example.tevp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Cacheable("productos")
    public List<Producto> obtenerPorTitulo(String titulo) {
        return productoRepository.findByTituloContaining(titulo);
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto guarduaProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

}
