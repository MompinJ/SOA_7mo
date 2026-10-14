package com.api.apartamentos.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.apartamentos.models.ReservacionModel;
import com.api.apartamentos.repositories.IReservacionRepository;
import com.api.apartamentos.repositories.IPagoRepository;
import com.api.apartamentos.repositories.IResenaRepository;

@Service
public class ReservacionService {

    @Autowired
    IReservacionRepository reservacionRepository;
    @Autowired
    IPagoRepository pagoRepository;
    @Autowired
    IResenaRepository resenaRepository;

    public ArrayList<ReservacionModel> obtenerReservaciones() {
        return (ArrayList<ReservacionModel>) reservacionRepository.findAll();
    }

    public ReservacionModel guardarReservacion(ReservacionModel reservacion) {
        if (reservacion.getEstado() == null) {
            reservacion.setEstado("PENDIENTE");
        }
        return reservacionRepository.save(reservacion);
    }

    public ReservacionModel obtenerPorId(Long id) {
        return reservacionRepository.findById(id).orElse(null);
    }

    public boolean unidadOcupada(Long idUnidad) {
        return reservacionRepository.existsByUnidadIdUnidadAndEstado(idUnidad, "CONFIRMADA");
    }

    public void eliminarReservacion(Long id) {
        reservacionRepository.deleteById(id);
    }

    public boolean tieneRelaciones(Long id) {
        return pagoRepository.existsByReservaIdReservacion(id) || resenaRepository.existsByReservaIdReservacion(id);
    }
}
