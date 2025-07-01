package com.example.Microservice_Producto.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Microservice_Producto.model.Producto;
import com.example.Microservice_Producto.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.orElse(null);
    }

    public Producto obtenerPorCodigo(String codigo) {
        Optional<Producto> producto = productoRepository.findByCodigo(codigo);
        return producto.orElse(null);
    }

    public Producto crear(Producto producto) {
        System.out.println("🔍 DEBUG - Código recibido: '" + producto.getCodigo() + "'");
        
        // Generar código único si no se proporciona
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            String codigoGenerado = generarCodigoUnico();
            producto.setCodigo(codigoGenerado);
            System.out.println("✅ DEBUG - Código generado: '" + codigoGenerado + "'");
        }
        
        // Verificar que el código no esté duplicado
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new RuntimeException("Ya existe un producto con el código: " + producto.getCodigo());
        }
        
        Producto savedProducto = productoRepository.save(producto);
        System.out.println("💾 DEBUG - Producto guardado con código: '" + savedProducto.getCodigo() + "'");
        
        return savedProducto;
    }

    public Producto actualizar(String codigo, Producto producto) {
        Producto productoExistente = obtenerPorCodigo(codigo);
        if (productoExistente != null) {
            producto.setId(productoExistente.getId()); // Mantener el ID
            producto.setCodigo(codigo); // Mantener el código
            return productoRepository.save(producto);
        }
        return null;
    }

    public void eliminar(String codigo) {
        Producto producto = obtenerPorCodigo(codigo);
        if (producto != null) {
            productoRepository.deleteById(producto.getId());
        }
    }

    // Métodos de compatibilidad para el controlador v1 (usando ID)
    public Producto actualizar(Long id, Producto producto) {
        if (productoRepository.existsById(id)) {
            producto.setId(id);
            // Si no tiene código, generar uno
            if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
                producto.setCodigo(generarCodigoUnico());
            }
            return productoRepository.save(producto);
        }
        return null;
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    /**
     * Genera un código único para el producto
     * Formato: PXXXXXX (P seguido de 6 caracteres alfanuméricos)
     */
    private String generarCodigoUnico() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder("P");
        
        do {
            codigo.setLength(1); // Mantener solo la 'P'
            for (int i = 0; i < 6; i++) {
                int index = ThreadLocalRandom.current().nextInt(caracteres.length());
                codigo.append(caracteres.charAt(index));
            }
        } while (productoRepository.existsByCodigo(codigo.toString()));
        
        return codigo.toString();
    }
}
