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
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.services.OficinaAlquilerService;
import com.api.alquilaya.services.VehiculoService;
import com.api.alquilaya.util.Constantes;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private OficinaAlquilerService oficinaService;

    @GetMapping
    public ArrayList<VehiculoModel> obtenerVehiculos() {
        return vehiculoService.obtenerVehiculos();
    }


    @PostMapping
    public ResponseEntity<Object> guardarVehiculo(@RequestBody VehiculoModel vehiculo) {
        // Validaciones
        if (vehiculo.getMarca() == null || vehiculo.getModelo() == null || vehiculo.getMatricula() == null || vehiculo.getAnio() == null) {
            System.out.println("faltan datos del vehiculo");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (vehiculo.getTarifaDiaria() == null || vehiculo.getTarifaDiaria().doubleValue() <= 0)
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);

        if (vehiculoService.buscarPorMatricula(vehiculo.getMatricula()) != null) {
            System.out.println("la matricula " + vehiculo.getMatricula() + " ya esta registrada");
            return Constantes.responder(Constantes.ERROR_DUPLICADO);
        }
        if (vehiculo.getOficina() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        OficinaAlquilerModel oficina = oficinaService.obtenerPorId(vehiculo.getOficina().getIdOficina());
        if (oficina == null) {
            System.out.println("no existe la oficina");
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }
        vehiculo.setOficina(oficina);
        System.out.println("oficina: " + oficina.getNombre());
        VehiculoModel nuevo = vehiculoService.guardarVehiculo(vehiculo);
        System.out.println("----");
        System.out.println("se guardo el vehiculo con id " + nuevo.getIdVehiculo());
        return Constantes.responder(Constantes.CREADO, nuevo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        VehiculoModel vehiculo = vehiculoService.obtenerPorId(id);
        if (vehiculo == null) {
            System.out.println("no existe vehiculo con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(vehiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarVehiculo(@RequestBody VehiculoModel request, @PathVariable("id") Long id) {
        VehiculoModel vehiculo = vehiculoService.obtenerPorId(id);
        if (vehiculo == null)
        {
            System.out.println("no existe vehiculo con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (request.getMarca() == null || request.getModelo() == null || request.getMatricula() == null || request.getAnio() == null || request.getTarifaDiaria() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (request.getTarifaDiaria().doubleValue() <= 0)
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        VehiculoModel otro = vehiculoService.buscarPorMatricula(request.getMatricula());
        if (otro != null && !otro.getIdVehiculo().equals(id)) {
            System.out.println("la matricula " + request.getMatricula() + " ya la tiene otro vehiculo");
            return Constantes.responder(Constantes.ERROR_DUPLICADO);
        }
        request.setIdVehiculo(id);
        request.setEstado(vehiculo.getEstado());
        request.setOficina(vehiculoService.obtenerPorId(id).getOficina());
        return Constantes.responder(Constantes.ACTUALIZADO, vehiculoService.guardarVehiculo(request));
    }

    @GetMapping("/estado/{estado}")
    public ArrayList<VehiculoModel> buscarPorEstado(@PathVariable String estado) {
        estado = estado.toUpperCase();
        ArrayList<VehiculoModel> lista = vehiculoService.buscarPorEstado(estado);
        System.out.println("hay " + lista.size() + " vehiculos " + estado);
        return lista;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarVehiculo(@PathVariable Long id) {
        if (vehiculoService.obtenerPorId(id) == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (vehiculoService.tieneAlquileres(id)) {
            System.out.println("el vehiculo " + id + " tiene alquileres");
            return Constantes.responder(Constantes.ERROR_TIENE_RELACIONES);
        }
        vehiculoService.eliminarVehiculo(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }
}
