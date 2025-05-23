package com.example.tevp.service;

import com.example.tevp.model.Resena;
import com.example.tevp.repository.ResenaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaService(ResenaRepository reseñaRepository) {
        this.resenaRepository = reseñaRepository;
    }

    public List<Resena> obtenerPorProducto(Long productoId) {
        return resenaRepository.findByProducto_Id(productoId);
    }

    public List<Resena> obtenerTodas() {
        return resenaRepository.findAll();
    }

    public Optional<Resena> obtenerPorId(Long id) {
        return resenaRepository.findById(id);
    }

    public Resena guardarReseña(Resena resena) {
        return resenaRepository.save(resena);
    }

    public void eliminarReseña(Long id) {
        resenaRepository.deleteById(id);
    }
}