package com.gimnasio.fitzone.controllers;

import com.gimnasio.fitzone.models.Clase;
import com.gimnasio.fitzone.services.ClaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clases")
public class ClaseController {

    private final ClaseService servicio;

    public ClaseController(ClaseService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Clase> consultarClases() {
        return servicio.consultarClases();
    }

    @GetMapping("/{id}/cupo")
    public ResponseEntity<Object> consultarCupo(@PathVariable Long id) {
        Clase clase = servicio.consultarCupo(id);
        if (clase == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CLASE_NO_ENCONTRADA");
        }
        if (clase.isCancelada()) {
            return ResponseEntity.badRequest().body("CLASE_CANCELADA");
        }
        return ResponseEntity.ok(clase);
    }
}
