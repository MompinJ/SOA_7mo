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

import com.api.apartamentos.models.ClienteModel;
import com.api.apartamentos.models.PropiedadModel;
import com.api.apartamentos.services.ClienteService;
import com.api.apartamentos.services.PropiedadService;
import com.api.apartamentos.util.Constantes;

@RestController
@RequestMapping("/api/propiedades")
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ArrayList<PropiedadModel> obtenerPropiedades() {
        return propiedadService.obtenerPropiedades();
    }

    @GetMapping("/tipo/{tipo}")
    public ArrayList<PropiedadModel> buscarPorTipo(@PathVariable String tipo) {
        return propiedadService.buscarPorTipo(tipo);
    }

    @PostMapping
    public ResponseEntity<Object> guardarPropiedad(@RequestBody PropiedadModel propiedad) {
        if (propiedad.getTitulo() == null || propiedad.getCiudad() == null) {
            System.out.println("faltan datos");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (propiedad.getPrecioNoche() <= 0)
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);

        if (propiedad.getPropietario() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        ClienteModel propietario = clienteService.obtenerPorId(propiedad.getPropietario().getId());
        if (propietario == null) {
            System.out.println("no existe el propietario");
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }
        propiedad.setPropietario(propietario);

        PropiedadModel nueva = propiedadService.guardarPropiedad(propiedad);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        PropiedadModel propiedad = propiedadService.obtenerPorId(id);
        // Verificar existencia
        if (propiedad == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(propiedad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarPropiedad(@RequestBody PropiedadModel request, @PathVariable("id") Long id) {
        PropiedadModel propiedad = propiedadService.obtenerPorId(id);
        if (propiedad == null) {
            System.out.println("no existe propiedad con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        request.setIdPropiedad(id);
        request.setPropietario(propiedadService.obtenerPorId(id).getPropietario());
        return ResponseEntity.ok(propiedadService.guardarPropiedad(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarPropiedad(@PathVariable Long id) {
        if (propiedadService.obtenerPorId(id) == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (propiedadService.tieneRelaciones(id)) {
            System.out.println("la propiedad tiene unidades o imagenes");
            return Constantes.responder(Constantes.ERROR_TIENE_RELACIONES);
        }
        propiedadService.eliminarPropiedad(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }

}
