package com.universidad.constancias.services;

import com.universidad.constancias.models.Estudiante;
import com.universidad.constancias.models.Notificacion;
import com.universidad.constancias.repositories.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public Notificacion enviarCorreo(Estudiante estudiante, String mensaje) {
        log.info("[Notificacion] Correo enviado (simulado) a {}: {}", estudiante.getCorreo(), mensaje);
        return repositorio.save(new Notificacion(estudiante, mensaje));
    }
}
