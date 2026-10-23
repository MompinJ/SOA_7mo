package com.universidad.constancias.controllers;

import com.universidad.constancias.models.Estudiante;
import com.universidad.constancias.models.Pago;
import com.universidad.constancias.services.EstudianteService;
import com.universidad.constancias.services.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final PagoService servicio;
    private final EstudianteService estudianteService;

    public PagoController(PagoService servicio, EstudianteService estudianteService) {
        this.servicio = servicio;
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public List<Pago> listar() {
        return servicio.listar();
    }

    @PostMapping("/{matricula}")
    public ResponseEntity<Object> registrarPago(@PathVariable String matricula, @RequestBody Pago request) {
        Estudiante estudiante = estudianteService.consultarEstudiante(matricula);
        if (estudiante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ESTUDIANTE_NO_ENCONTRADO");
        }
        Pago pago = servicio.consultarPorReferencia(request.getReferencia());
        if (pago == null || !pago.getEstudiante().getMatricula().equals(estudiante.getMatricula())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("REFERENCIA_NO_ENCONTRADA");
        }
        if ("CONFIRMADO".equals(pago.getEstatus())) {
            return ResponseEntity.badRequest().body("PAGO_DUPLICADO");
        }
        if (!servicio.referenciaVigente(pago)) {
            return ResponseEntity.badRequest().body("REFERENCIA_VENCIDA");
        }
        if (request.getMonto() != pago.getMonto()) {
            return ResponseEntity.badRequest().body("MONTO_INCORRECTO");
        }
        LocalDate fechaPago = request.getFechaPago();
        if (fechaPago == null) {
            fechaPago = LocalDate.now();
        }
        if (fechaPago.isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().body("FECHA_PAGO_INVALIDA");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.registrarPago(pago, request.getMetodoPago(), fechaPago));
    }
}
