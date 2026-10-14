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

import com.api.apartamentos.models.PagoModel;
import com.api.apartamentos.models.ReservacionModel;
import com.api.apartamentos.services.PagoService;
import com.api.apartamentos.services.ReservacionService;
import com.api.apartamentos.util.Constantes;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;
    @Autowired
    private ReservacionService reservacionService;

    @GetMapping
    public ArrayList<PagoModel> obtenerPagos() {
        return pagoService.obtenerPagos();
    }

    @PostMapping
    public ResponseEntity<Object> guardarPago(@RequestBody PagoModel pago) {
        if (pago.getReserva() == null || pago.getImporte() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        ReservacionModel reservacion = reservacionService.obtenerPorId(pago.getReserva().getIdReservacion());
        // Verificar existencia
        if (reservacion == null) {
            System.out.println("no existe la reservacion");
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }
        double importe = pago.getImporte();
        double total = reservacion.getImporteTotal();
        if (importe <= 0) {
            System.out.println("importe invalido: " + importe);
            return Constantes.responder(Constantes.ERROR_IMPORTE);
        }
        if (importe > total) {
            System.out.println("el importe " + importe + " pasa del total " + total);
            return Constantes.responder(Constantes.ERROR_IMPORTE);
        }
        pago.setReserva(reservacion);

        PagoModel nuevo = pagoService.guardarPago(pago);
        System.out.println("----");
        System.out.println("se guardo el pago con id " + nuevo.getIdPago());
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        PagoModel pago = pagoService.obtenerPorId(id);
        if (pago == null) {
            System.out.println("no existe pago con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(pago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarPago(@RequestBody PagoModel request, @PathVariable("id") Long id) {
        PagoModel pago = pagoService.obtenerPorId(id);
        if (pago == null) {
            System.out.println("no existe pago con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (request.getImporte() == null) {
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        double total = pago.getReserva().getImporteTotal();
        if (request.getImporte() <= 0 || request.getImporte() > total) {
            System.out.println("importe invalido: " + request.getImporte() + " (total " + total + ")");
            return Constantes.responder(Constantes.ERROR_IMPORTE);
        }
        request.setIdPago(id);
        request.setReserva(pago.getReserva());
        return ResponseEntity.ok(pagoService.guardarPago(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarPago(@PathVariable Long id) {
        if (pagoService.obtenerPorId(id) == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        pagoService.eliminarPago(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }
}
