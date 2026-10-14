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

import com.api.apartamentos.models.PropiedadModel;
import com.api.apartamentos.models.UnidadRentableModel;
import com.api.apartamentos.services.PropiedadService;
import com.api.apartamentos.services.UnidadRentableService;
import com.api.apartamentos.util.Constantes;

@RestController
@RequestMapping("/api/unidades-rentables")
public class UnidadRentableController {

    @Autowired
    private UnidadRentableService unidadService;
    @Autowired
    private PropiedadService propiedadService;

    @GetMapping
    public ArrayList<UnidadRentableModel> obtenerUnidades() {
        return unidadService.obtenerUnidades();
    }

    @PostMapping
    public ResponseEntity<Object> guardarUnidad(@RequestBody UnidadRentableModel unidad) {
        if (unidad.getNombre() == null || unidad.getPropiedad() == null) {
            System.out.println("faltan datos de la unidad");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (unidad.getPrecioNoche() <= 0) {
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }
        PropiedadModel propiedad = propiedadService.obtenerPorId(unidad.getPropiedad().getIdPropiedad());
        if (propiedad == null) {
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }
        unidad.setPropiedad(propiedad);

        UnidadRentableModel nueva = unidadService.guardarUnidad(unidad);
        System.out.println("se guardo la unidad con id " + nueva.getIdUnidad());
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        UnidadRentableModel unidad = unidadService.obtenerPorId(id);
        if (unidad == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(unidad);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarUnidad(@RequestBody UnidadRentableModel request, @PathVariable("id") Long id) {
        UnidadRentableModel unidad = unidadService.obtenerPorId(id);
        if (unidad == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        request.setIdUnidad(id);
        request.setPropiedad(unidad.getPropiedad());
        return ResponseEntity.ok(unidadService.guardarUnidad(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarUnidad(@PathVariable Long id) {
        // Verificar existencia
        if (unidadService.existeUnidad(id) == false) {
            System.out.println("no existe unidad con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (unidadService.tieneRelaciones(id) == true) {
            System.out.println("la unidad tiene reservaciones");
            return Constantes.responder(Constantes.ERROR_TIENE_RELACIONES);
        }
        unidadService.eliminarUnidad(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }
}
