package com.example.tevp.service;

import com.example.tevp.model.Notificacion;
import com.example.tevp.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> obtenerPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    public Notificacion guardarNotificacion(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    public void eliminarNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }
}
