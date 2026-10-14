package com.api.apartamentos.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.apartamentos.models.DisponibilidadModel;
import com.api.apartamentos.services.DisponibilidadService;
import com.api.apartamentos.services.UnidadRentableService;
import com.api.apartamentos.util.Constantes;

@RestController
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private UnidadRentableService unidadService;

    @GetMapping
    public ArrayList<DisponibilidadModel> obtenerDisponibilidades() {
        return disponibilidadService.obtenerDisponibilidades();
    }

    @PostMapping
    public ResponseEntity<Object> guardarDisponibilidad(@RequestBody DisponibilidadModel d) {
        if (d.getUnidad() == null || unidadService.obtenerPorId(d.getUnidad().getIdUnidad()) == null)
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        if (d.getFechaInicio() == null || d.getFechaFin() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        return new ResponseEntity<>(disponibilidadService.guardarDisponibilidad(d), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        DisponibilidadModel d = disponibilidadService.obtenerPorId(id);
        if (d == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(d);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarDisponibilidad(@RequestBody DisponibilidadModel request, @PathVariable("id") Long id) {
        DisponibilidadModel d = disponibilidadService.obtenerPorId(id);
        if(d==null)
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        if (request.getFechaInicio() == null || request.getFechaFin() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (!request.getFechaFin().isAfter(request.getFechaInicio())) {
            System.out.println("fechas mal en la disponibilidad " + id);
            return Constantes.responder(Constantes.ERROR_FECHAS);
        }
        request.setIdDisponibilidad(id);
        request.setUnidad(d.getUnidad());
        return ResponseEntity.ok(disponibilidadService.guardarDisponibilidad(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarDisponibilidad(@PathVariable Long id) {
        if (disponibilidadService.obtenerPorId(id) == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        disponibilidadService.eliminarDisponibilidad(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }

}
