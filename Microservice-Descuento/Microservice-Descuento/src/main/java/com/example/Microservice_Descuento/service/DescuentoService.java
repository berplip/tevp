package com.example.Microservice_Descuento.service;

import com.example.Microservice_Descuento.model.Descuento;
import com.example.Microservice_Descuento.repository.DescuentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DescuentoService {

    @Autowired
    private DescuentoRepository descuentoRepository;

    public List<Descuento> obtenerTodos() {
        return descuentoRepository.findAll();
    }

    public Descuento obtenerPorId(Long id) {
        Optional<Descuento> descuento = descuentoRepository.findById(id);
        return descuento.orElse(null);
    }

    public Descuento crear(Descuento descuento) {
        return descuentoRepository.save(descuento);
    }

    public Descuento actualizar(Long id, Descuento descuento) {
        if (descuentoRepository.existsById(id)) {
            descuento.setId(id);
            return descuentoRepository.save(descuento);
        }
        return null;
    }

    public void eliminar(Long id) {
        descuentoRepository.deleteById(id);
    }
}
