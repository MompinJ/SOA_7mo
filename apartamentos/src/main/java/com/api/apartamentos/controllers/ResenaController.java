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

import com.api.apartamentos.models.ResenaModel;
import com.api.apartamentos.services.ResenaService;
import com.api.apartamentos.services.ReservacionService;
import com.api.apartamentos.util.Constantes;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;
    @Autowired
    private ReservacionService reservacionService;

    @GetMapping
    public ArrayList<ResenaModel> obtenerResenas() {
        return resenaService.obtenerResenas();
    }

    @PostMapping
    public ResponseEntity<Object> guardarResena(@RequestBody ResenaModel resena) {
        int calificacion = resena.getCalificacion();
        boolean valida = false;
        if (calificacion == 1 || calificacion == 2 || calificacion == 3 || calificacion == 4 || calificacion == 5) {
            valida = true;
        }
        if (valida == false) {
            System.out.println("calificacion fuera de rango: " + resena.getCalificacion());
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }
        if (resena.getReserva() == null || reservacionService.obtenerPorId(resena.getReserva().getIdReservacion()) == null) {
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }
        resena.setReserva(reservacionService.obtenerPorId(resena.getReserva().getIdReservacion()));
        return new ResponseEntity<>(resenaService.guardarResena(resena), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        ResenaModel resena = resenaService.obtenerPorId(id);
        if (resena == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(resena);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarResena(@RequestBody ResenaModel request, @PathVariable("id") Long id) {
        ResenaModel resena = resenaService.obtenerPorId(id);
        if (resena == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (request.getCalificacion() < 1 || request.getCalificacion() > 5) {
            System.out.println("calificacion fuera de rango: " + request.getCalificacion());
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }
        request.setIdResena(id);
        request.setReserva(resenaService.obtenerPorId(id).getReserva());
        return ResponseEntity.ok(resenaService.guardarResena(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarResena(@PathVariable Long id) {
        if (resenaService.obtenerPorId(id) == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        resenaService.eliminarResena(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }
}
