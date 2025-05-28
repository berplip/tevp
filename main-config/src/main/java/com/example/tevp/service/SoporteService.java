package com.example.tevp.service;

import com.example.tevp.model.SoporteOnline;
import com.example.tevp.repository.SoporteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SoporteService {

    private final SoporteRepository soporteRepository;

    public SoporteService(SoporteRepository soporteRepository) {
        this.soporteRepository = soporteRepository;
    }

    public List<SoporteOnline> obtenerPorEstado(String estado) {
        return soporteRepository.findByEstado(estado);
    }

    public List<SoporteOnline> obtenerTodos() {
        return soporteRepository.findAll();
    }

    public Optional<SoporteOnline> obtenerPorId(Long id) {
        return soporteRepository.findById(id);
    }

    public SoporteOnline guardarSoporte(SoporteOnline soporte) {
        return soporteRepository.save(soporte);
    }

    public void eliminarSoporte(Long id) {
        soporteRepository.deleteById(id);
    }
}
