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
import com.api.apartamentos.models.ReservacionModel;
import com.api.apartamentos.models.UnidadRentableModel;
import com.api.apartamentos.services.ClienteService;
import com.api.apartamentos.services.ReservacionService;
import com.api.apartamentos.services.UnidadRentableService;
import com.api.apartamentos.util.Constantes;

@RestController
@RequestMapping("/api/reservaciones")
public class ReservacionController {

    @Autowired
    private ReservacionService reservacionService;

    @Autowired
    private UnidadRentableService unidadService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ArrayList<ReservacionModel> obtenerReservaciones() {
        return reservacionService.obtenerReservaciones();
    }

    @PostMapping
    public ResponseEntity<Object> guardarReservacion(@RequestBody ReservacionModel reservacion) {
        // Validaciones
        if (reservacion.getFechaEntrada() == null || reservacion.getFechaSalida() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        boolean fechasCorrectas = reservacion.getFechaSalida().isAfter(reservacion.getFechaEntrada());
        if (fechasCorrectas == false) {
            System.out.println("las fechas estan mal");
            return Constantes.responder(Constantes.ERROR_FECHAS);
        }
        if (reservacion.getImporteTotal() <= 0)
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        if (reservacion.getUnidad() == null || reservacion.getCliente() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }

        UnidadRentableModel unidad = unidadService.obtenerPorId(reservacion.getUnidad().getIdUnidad());
        ClienteModel cliente = clienteService.obtenerPorId(reservacion.getCliente().getId());
        if (unidad == null || cliente == null) {
            System.out.println("no existe la unidad o el cliente");
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }
        if (reservacionService.unidadOcupada(unidad.getIdUnidad())) {
            System.out.println("la unidad " + unidad.getNombre() + " ya esta reservada");
            return Constantes.responder(Constantes.ERROR_NO_DISPONIBLE);
        }
        reservacion.setUnidad(unidad);
        reservacion.setCliente(cliente);

        ReservacionModel nueva = reservacionService.guardarReservacion(reservacion);
        System.out.println("----");
        System.out.println("se guardo la reservacion con id " + nueva.getIdReservacion());
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        ReservacionModel reservacion = reservacionService.obtenerPorId(id);
        // Verificar existencia
        if (reservacion == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(reservacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarReservacion(@RequestBody ReservacionModel request, @PathVariable("id") Long id) {
        ReservacionModel reservacion = reservacionService.obtenerPorId(id);
        if (reservacion == null) {
            System.out.println("no existe reservacion con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (request.getFechaEntrada() == null || request.getFechaSalida() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (!request.getFechaSalida().isAfter(request.getFechaEntrada())) {
            System.out.println("las fechas estan mal");
            return Constantes.responder(Constantes.ERROR_FECHAS);
        }
        if (request.getImporteTotal() <= 0)
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);

        boolean yaEstabaConfirmada = false;
        if (reservacion.getEstado() != null && reservacion.getEstado().equals("CONFIRMADA")) {
            yaEstabaConfirmada = true;
        }
        if (request.getEstado() != null && request.getEstado().equals("CONFIRMADA") && yaEstabaConfirmada == false) {
            if (reservacionService.unidadOcupada(reservacion.getUnidad().getIdUnidad())) {
                System.out.println("la unidad ya tiene otra reservacion confirmada");
                return Constantes.responder(Constantes.ERROR_NO_DISPONIBLE);
            }
        }
        request.setIdReservacion(id);
        request.setUnidad(reservacion.getUnidad());
        request.setCliente(reservacion.getCliente());
        return ResponseEntity.ok(reservacionService.guardarReservacion(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarReservacion(@PathVariable Long id) {
        if (reservacionService.obtenerPorId(id) == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (reservacionService.tieneRelaciones(id)) {
            System.out.println("no se puede borrar la reservacion " + id + ", tiene pagos o resenas");
            return Constantes.responder(Constantes.ERROR_TIENE_RELACIONES);
        }
        reservacionService.eliminarReservacion(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }
}
