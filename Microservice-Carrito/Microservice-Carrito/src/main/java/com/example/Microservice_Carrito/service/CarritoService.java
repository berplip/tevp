package com.example.Microservice_Carrito.service;

import com.example.Microservice_Carrito.model.Carrito;
import com.example.Microservice_Carrito.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    public List<Carrito> obtenerTodos() {
        return carritoRepository.findAll();
    }

    public Carrito obtenerPorId(Long id) {
        Optional<Carrito> carrito = carritoRepository.findById(id);
        return carrito.orElse(null);
    }

    public Carrito crear(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    public Carrito actualizar(Long id, Carrito carrito) {
        if (carritoRepository.existsById(id)) {
            carrito.setId(id);
            return carritoRepository.save(carrito);
        }
        return null;
    }

    public void eliminar(Long id) {
        carritoRepository.deleteById(id);
    }
}
