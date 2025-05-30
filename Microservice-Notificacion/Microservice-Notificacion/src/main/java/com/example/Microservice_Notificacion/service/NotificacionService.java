package com.example.Microservice_Notificacion.service;

import com.example.Microservice_Notificacion.model.Notificacion;
import com.example.Microservice_Notificacion.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> obtenerTodas() {
        return notificacionRepository.findAll();
    }

    public Notificacion obtenerPorId(Long id) {
        Optional<Notificacion> notificacion = notificacionRepository.findById(id);
        return notificacion.orElse(null);
    }

    public Notificacion crear(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    public Notificacion actualizar(Long id, Notificacion notificacion) {
        if (notificacionRepository.existsById(id)) {
            notificacion.setId(id);
            return notificacionRepository.save(notificacion);
        }
        return null;
    }

    public void eliminar(Long id) {
        notificacionRepository.deleteById(id);
    }
}
