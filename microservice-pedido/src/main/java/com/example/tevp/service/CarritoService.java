package com.example.tevp.service;

import com.example.tevp.model.Carrito;
import com.example.tevp.repository.CarritoRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;

    public CarritoService(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public Optional<Carrito> obtenerPorId(Long id) {
        return carritoRepository.findById(id);
    }

    public Carrito guardarCarrito(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    public void eliminarCarrito(Long id) {
        carritoRepository.deleteById(id);
    }
}