package com.gimnasio.fitzone.controllers;

import com.gimnasio.fitzone.models.Notificacion;
import com.gimnasio.fitzone.services.NotificacionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

    private final NotificacionService servicio;

    public NotificacionController(NotificacionService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Notificacion> listar() {
        return servicio.listar();
    }
}
