package com.api.alquilaya.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.services.OficinaAlquilerService;
import com.api.alquilaya.util.Constantes;

@RestController
@RequestMapping("/api/oficinas")
public class OficinaAlquilerController {

    @Autowired
    private OficinaAlquilerService oficinaService;

    @GetMapping
    public ArrayList<OficinaAlquilerModel> obtenerOficinas() {
        return oficinaService.obtenerOficinas();
    }

    @PostMapping
    public ResponseEntity<Object> guardarOficina(@RequestBody OficinaAlquilerModel oficina) {
        if (oficina.getNombre() == null || oficina.getDireccion() == null || oficina.getCiudad() == null || oficina.getRegion() == null) {
            System.out.println("faltan datos de la oficina");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        OficinaAlquilerModel nueva = oficinaService.guardarOficina(oficina);
        System.out.println("se guardo la oficina " + nueva.getNombre());
        return Constantes.responder(Constantes.CREADO, nueva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        OficinaAlquilerModel oficina = oficinaService.obtenerPorId(id);
        if(oficina == null){
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(oficina);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarOficina(@RequestBody OficinaAlquilerModel request, @PathVariable("id") Long id) {
        if (oficinaService.obtenerPorId(id) == null) {
            System.out.println("no existe oficina con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (request.getNombre() == null || request.getDireccion() == null || request.getCiudad() == null || request.getRegion() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        request.setIdOficina(id);
        return Constantes.responder(Constantes.ACTUALIZADO, oficinaService.guardarOficina(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarOficina(@PathVariable Long id) {
        // Verificar existencia
        if (oficinaService.obtenerPorId(id) == null) {
            System.out.println("no existe oficina con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (oficinaService.tieneVehiculos(id) == true) {
            System.out.println("la oficina tiene vehiculos");
            return Constantes.responder(Constantes.ERROR_TIENE_RELACIONES);
        }
        oficinaService.eliminarOficina(id);
        System.out.println("----");
        return Constantes.responder(Constantes.ELIMINADO);
    }
}
