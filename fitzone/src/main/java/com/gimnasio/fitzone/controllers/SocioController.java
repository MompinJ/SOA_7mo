package com.gimnasio.fitzone.controllers;

import com.gimnasio.fitzone.models.Socio;
import com.gimnasio.fitzone.services.SocioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/socios")
public class SocioController {

    private final SocioService servicio;

    public SocioController(SocioService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Socio> listar() {
        return servicio.listar();
    }

    @GetMapping("/{numeroSocio}")
    public ResponseEntity<Socio> consultarSocio(@PathVariable String numeroSocio) {
        Socio socio = servicio.consultarSocio(numeroSocio);
        if (socio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(socio);
    }
}
