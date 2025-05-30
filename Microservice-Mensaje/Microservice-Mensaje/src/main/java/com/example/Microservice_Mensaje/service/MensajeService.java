package com.example.Microservice_Mensaje.service;

import com.example.Microservice_Mensaje.model.Mensaje;
import com.example.Microservice_Mensaje.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    public List<Mensaje> obtenerTodos() {
        return mensajeRepository.findAll();
    }

    public Mensaje obtenerPorId(Long id) {
        Optional<Mensaje> mensaje = mensajeRepository.findById(id);
        return mensaje.orElse(null);
    }

    public Mensaje crear(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    public Mensaje actualizar(Long id, Mensaje mensaje) {
        if (mensajeRepository.existsById(id)) {
            mensaje.setId(id);
            return mensajeRepository.save(mensaje);
        }
        return null;
    }

    public void eliminar(Long id) {
        mensajeRepository.deleteById(id);
    }
}
