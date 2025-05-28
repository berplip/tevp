package com.example.tevp.service;

import com.example.tevp.model.Descuento;
import com.example.tevp.repository.DescuentoRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;

    public DescuentoService(DescuentoRepository descuentoRepository) {
        this.descuentoRepository = descuentoRepository;
    }

    public Optional<Descuento> obtenerPorId(Long id) {
        return descuentoRepository.findById(id);
    }

    public Descuento guardarDescuento(Descuento descuento) {
        return descuentoRepository.save(descuento);
    }

    public void eliminarDescuento(Long id) {
        descuentoRepository.deleteById(id);
    }
}