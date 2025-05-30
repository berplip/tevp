package com.example.Microservice_Resena.service;

import com.example.Microservice_Resena.model.Resena;
import com.example.Microservice_Resena.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public List<Resena> obtenerTodas() {
        return resenaRepository.findAll();
    }

    public Resena obtenerPorId(Long id) {
        Optional<Resena> resena = resenaRepository.findById(id);
        return resena.orElse(null);
    }

    public Resena crear(Resena resena) {
        return resenaRepository.save(resena);
    }

    public Resena actualizar(Long id, Resena resena) {
        if (resenaRepository.existsById(id)) {
            resena.setId(id);
            return resenaRepository.save(resena);
        }
        return null;
    }

    public void eliminar(Long id) {
        resenaRepository.deleteById(id);
    }
}
