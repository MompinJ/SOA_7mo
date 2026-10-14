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

import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.services.AlquilerService;
import com.api.alquilaya.services.ClienteService;
import com.api.alquilaya.services.VehiculoService;
import com.api.alquilaya.util.Constantes;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ArrayList<AlquilerModel> obtenerAlquileres() {
        return alquilerService.obtenerAlquileres();
    }

    @PostMapping
    public ResponseEntity<Object> guardarAlquiler(@RequestBody AlquilerModel alquiler) {
        // Validaciones
        if (alquiler.getFechaAlquiler() == null || alquiler.getFechaDevolucion() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (!alquiler.getFechaDevolucion().isAfter(alquiler.getFechaAlquiler())) {
            System.out.println("las fechas estan mal");
            return Constantes.responder(Constantes.ERROR_FECHAS);
        }
        if (alquiler.getPrecioTotal() == null || alquiler.getPrecioTotal().doubleValue() <= 0) {
            System.out.println("precio invalido: " + alquiler.getPrecioTotal());
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }
        if (alquiler.getVehiculo() == null || alquiler.getCliente() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }

        VehiculoModel vehiculo = vehiculoService.obtenerPorId(alquiler.getVehiculo().getIdVehiculo());
        ClienteModel cliente = clienteService.obtenerPorId(alquiler.getCliente().getIdCliente());
        if (vehiculo == null || cliente == null) {
            System.out.println("no existe el vehiculo o el cliente");
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }
        boolean disponible = true;
        if (vehiculo.getEstado().equals("ALQUILADO")) {
            disponible = false;
        }
        if (disponible == false) {
            System.out.println("el vehiculo " + vehiculo.getMatricula() + " ya esta alquilado");
            return Constantes.responder(Constantes.ERROR_NO_DISPONIBLE);
        }

        vehiculo.setEstado("ALQUILADO");
        vehiculoService.guardarVehiculo(vehiculo);
        System.out.println("----" + "el vehiculo " + vehiculo.getMatricula() + " cambio a ALQUILADO");
        alquiler.setVehiculo(vehiculo);
        alquiler.setCliente(cliente);

        AlquilerModel nuevo = alquilerService.guardarAlquiler(alquiler);
        System.out.println("----");
        System.out.println("se guardo el alquiler con id " + nuevo.getIdAlquiler());
        return Constantes.responder(Constantes.CREADO, nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarAlquiler(@RequestBody AlquilerModel request, @PathVariable("id") Long id) {
        AlquilerModel alquiler = alquilerService.obtenerPorId(id);
        if (alquiler == null) {
            System.out.println("no existe alquiler con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (request.getFechaAlquiler() == null || request.getFechaDevolucion() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (!request.getFechaDevolucion().isAfter(request.getFechaAlquiler())) {
            System.out.println("las fechas estan mal");
            return Constantes.responder(Constantes.ERROR_FECHAS);
        }
        if (request.getPrecioTotal() == null || request.getPrecioTotal().doubleValue() <= 0) {
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }
        request.setIdAlquiler(id);
        request.setVehiculo(alquiler.getVehiculo());
        request.setCliente(alquiler.getCliente());
        return Constantes.responder(Constantes.ACTUALIZADO, alquilerService.guardarAlquiler(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        AlquilerModel alquiler = alquilerService.obtenerPorId(id);
        // Verificar existencia
        if (alquiler == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(alquiler);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarAlquiler(@PathVariable Long id) {
        AlquilerModel alquiler = alquilerService.obtenerPorId(id);
        if (alquiler == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        VehiculoModel vehiculo = alquiler.getVehiculo();
        vehiculo.setEstado("DISPONIBLE");
        vehiculoService.guardarVehiculo(vehiculo);
        System.out.println("se libero el vehiculo " + vehiculo.getMatricula());

        alquilerService.eliminarAlquiler(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }
}
