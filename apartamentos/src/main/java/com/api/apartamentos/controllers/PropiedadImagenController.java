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

import com.api.apartamentos.models.PropiedadImagenModel;
import com.api.apartamentos.services.PropiedadImagenService;
import com.api.apartamentos.services.PropiedadService;
import com.api.apartamentos.util.Constantes;

@RestController
@RequestMapping("/api/propiedad-imagenes")
public class PropiedadImagenController {

    @Autowired
    private PropiedadImagenService imagenService;
    @Autowired
    private PropiedadService propiedadService;

    @GetMapping
    public ArrayList<PropiedadImagenModel> obtenerImagenes() {
        return imagenService.obtenerImagenes();
    }

    @PostMapping
    public ResponseEntity<Object> guardarImagen(@RequestBody PropiedadImagenModel imagen) {
        if (imagen.getPropiedad() == null || propiedadService.obtenerPorId(imagen.getPropiedad().getIdPropiedad()) == null) {
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }
        imagen.setPropiedad(propiedadService.obtenerPorId(imagen.getPropiedad().getIdPropiedad()));
        return new ResponseEntity<>(imagenService.guardarImagen(imagen), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        PropiedadImagenModel imagen = imagenService.obtenerPorId(id);
        if (imagen == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(imagen);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarImagen(@RequestBody PropiedadImagenModel request, @PathVariable("id") Long id) {
        PropiedadImagenModel imagen = imagenService.obtenerPorId(id);
        if (imagen == null) {
            System.out.println("no existe imagen con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (request.getUrl() == null || request.getUrl().trim().equals("")) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        request.setIdImagen(id);
        request.setPropiedad(imagen.getPropiedad());
        PropiedadImagenModel actualizada = imagenService.guardarImagen(request);
        System.out.println("----" + "imagen " + id + " actualizada");
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarImagen(@PathVariable Long id) {
        if (imagenService.obtenerPorId(id) == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        imagenService.eliminarImagen(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }
}
