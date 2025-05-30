package com.example.Microservice_SoporteOnline.service;

import com.example.Microservice_SoporteOnline.model.SoporteOnline;
import com.example.Microservice_SoporteOnline.repository.SoporteOnlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SoporteOnlineService {

    @Autowired
    private SoporteOnlineRepository soporteOnlineRepository;

    public List<SoporteOnline> obtenerTodos() {
        return soporteOnlineRepository.findAll();
    }

    public SoporteOnline obtenerPorId(Long id) {
        Optional<SoporteOnline> soporte = soporteOnlineRepository.findById(id);
        return soporte.orElse(null);
    }

    public SoporteOnline crear(SoporteOnline soporteOnline) {
        return soporteOnlineRepository.save(soporteOnline);
    }

    public SoporteOnline actualizar(Long id, SoporteOnline soporteOnline) {
        if (soporteOnlineRepository.existsById(id)) {
            soporteOnline.setId(id);
            return soporteOnlineRepository.save(soporteOnline);
        }
        return null;
    }

    public void eliminar(Long id) {
        soporteOnlineRepository.deleteById(id);
    }
}
