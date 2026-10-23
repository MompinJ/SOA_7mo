package com.gimnasio.fitzone.controllers;

import com.gimnasio.fitzone.models.Clase;
import com.gimnasio.fitzone.models.Inscripcion;
import com.gimnasio.fitzone.models.Socio;
import com.gimnasio.fitzone.services.ClaseService;
import com.gimnasio.fitzone.services.InscripcionService;
import com.gimnasio.fitzone.services.SocioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inscripciones")
public class InscripcionController {

    private final InscripcionService servicio;
    private final SocioService socioService;
    private final ClaseService claseService;

    public InscripcionController(InscripcionService servicio, SocioService socioService, ClaseService claseService) {
        this.servicio = servicio;
        this.socioService = socioService;
        this.claseService = claseService;
    }

    @GetMapping
    public List<Inscripcion> listar() {
        return servicio.listar();
    }

    @PostMapping("/{numeroSocio}/{idClase}")
    public ResponseEntity<Object> inscribirEnClase(@PathVariable String numeroSocio, @PathVariable Long idClase) {
        Socio socio = socioService.consultarSocio(numeroSocio);
        if (socio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SOCIO_NO_ENCONTRADO");
        }
        Clase clase = claseService.consultarCupo(idClase);
        if (clase == null || clase.isCancelada()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CLASE_NO_ENCONTRADA");
        }
        if (!socioService.consultarMembresia(socio, clase.getFechaHora().toLocalDate())) {
            return ResponseEntity.badRequest().body("MEMBRESIA_VENCIDA");
        }
        if (clase.getFechaHora().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("CLASE_YA_INICIADA");
        }
        if (servicio.yaInscrito(socio, clase)) {
            return ResponseEntity.badRequest().body("INSCRIPCION_DUPLICADA");
        }
        if (!clase.isHayCupo()) {
            return ResponseEntity.badRequest().body("CLASE_SIN_CUPO");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.inscribirEnClase(socio, clase));
    }
}
