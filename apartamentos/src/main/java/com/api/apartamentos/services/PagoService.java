package com.api.apartamentos.services;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.apartamentos.models.PagoModel;
import com.api.apartamentos.repositories.IPagoRepository;

@Service
public class PagoService {

    @Autowired
    IPagoRepository pagoRepository;

    public ArrayList<PagoModel> obtenerPagos() {
        return (ArrayList<PagoModel>) pagoRepository.findAll();
    }

    public PagoModel guardarPago(PagoModel pago) {
        if (pago.getEstado() == null) {
            pago.setEstado("PENDIENTE");
        }
        if(pago.getMetodo() == null)
            pago.setMetodo("TRANSFERENCIA");
        if (pago.getFecha() == null) {
            pago.setFecha(LocalDate.now());
        }
        return pagoRepository.save(pago);
    }

    public PagoModel obtenerPorId(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }

}
