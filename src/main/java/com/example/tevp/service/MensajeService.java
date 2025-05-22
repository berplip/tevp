package com.example.tevp.service;

import com.example.tevp.model.Mensaje;
import com.example.tevp.repository.MensajeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;

    public MensajeService(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    public List<Mensaje> obtenerPorRemitente(Long remitenteId) {
        return mensajeRepository.findByRemitenteId(remitenteId);
    }

    public List<Mensaje> obtenerTodos() {
        return mensajeRepository.findAll();
    }

    public Optional<Mensaje> obtenerPorId(Long id) {
        return mensajeRepository.findById(id);
    }

    public Mensaje guardarMensaje(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    public void eliminarMensaje(Long id) {
        mensajeRepository.deleteById(id);
    }
}