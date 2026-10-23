package com.gimnasio.fitzone.services;

import com.gimnasio.fitzone.models.Notificacion;
import com.gimnasio.fitzone.models.Socio;
import com.gimnasio.fitzone.repositories.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository repositorio;

    public NotificacionService(NotificacionRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Notificacion> listar() {
        return repositorio.findAll();
    }

    public Notificacion enviarConfirmacion(Socio socio, String mensaje) {
        log.info("[Notificacion] Confirmacion enviada (simulado) a {}: {}", socio.getCorreo(), mensaje);
        return repositorio.save(new Notificacion(socio, mensaje, LocalDateTime.now()));
    }

    public Notificacion programarRecordatorio(Socio socio, String mensaje, LocalDateTime fecha) {
        log.info("[Notificacion] Recordatorio programado (simulado) para {} a las {}", socio.getCorreo(), fecha);
        return repositorio.save(new Notificacion(socio, mensaje, fecha));
    }
}
