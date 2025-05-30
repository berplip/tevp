package com.example.Microservice_Pago.service;

import com.example.Microservice_Pago.model.Pago;
import com.example.Microservice_Pago.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    public Pago obtenerPorId(Long id) {
        Optional<Pago> pago = pagoRepository.findById(id);
        return pago.orElse(null);
    }

    public Pago crear(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Pago actualizar(Long id, Pago pago) {
        if (pagoRepository.existsById(id)) {
            pago.setId(id);
            return pagoRepository.save(pago);
        }
        return null;
    }

    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }
}
