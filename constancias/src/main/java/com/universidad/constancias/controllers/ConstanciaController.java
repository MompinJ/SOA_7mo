package com.universidad.constancias.controllers;

import com.universidad.constancias.models.Constancia;
import com.universidad.constancias.models.Pago;
import com.universidad.constancias.services.ConstanciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/constancias")
public class ConstanciaController {

    private final ConstanciaService servicio;

    public ConstanciaController(ConstanciaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Constancia> listar() {
        return servicio.listar();
    }

    @PostMapping("/{matricula}")
    public ResponseEntity<Object> solicitarConstancia(@PathVariable String matricula) {
        Constancia constancia = servicio.solicitarConstancia(matricula);
        if (constancia == null) {
            return ResponseEntity.badRequest().body("El estudiante no existe o no esta inscrito");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(constancia);
    }

    @PostMapping("/referencia/{matricula}")
    public ResponseEntity<Object> generarReferenciaPago(@PathVariable String matricula) {
        Pago pago = servicio.generarReferenciaPago(matricula);
        if (pago == null) {
            return ResponseEntity.badRequest().body("El estudiante no existe o no esta inscrito");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(pago);
    }
}
