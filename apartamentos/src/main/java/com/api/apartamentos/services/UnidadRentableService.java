package com.api.apartamentos.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.apartamentos.models.UnidadRentableModel;
import com.api.apartamentos.repositories.IUnidadRentableRepository;
import com.api.apartamentos.repositories.IReservacionRepository;
import com.api.apartamentos.repositories.IDisponibilidadRepository;

@Service
public class UnidadRentableService {

    @Autowired
    IUnidadRentableRepository unidadRepository;
    @Autowired
    IReservacionRepository reservacionRepository;
    @Autowired
    IDisponibilidadRepository disponibilidadRepository;


    public ArrayList<UnidadRentableModel> obtenerUnidades() {
        return (ArrayList<UnidadRentableModel>) unidadRepository.findAll();
    }

    public UnidadRentableModel guardarUnidad(UnidadRentableModel unidad) {
        return unidadRepository.save(unidad);
    }

    public UnidadRentableModel obtenerPorId(Long id) {
        return unidadRepository.findById(id).orElse(null);
    }

    public boolean existeUnidad(Long id) {
        if (obtenerPorId(id) == null) {
            return false;
        } else {
            return true;
        }
    }
    public void eliminarUnidad(Long id) {
        unidadRepository.deleteById(id);
    }

    public boolean tieneRelaciones(Long id) {
        if (reservacionRepository.existsByUnidadIdUnidad(id))
            return true;
        if (disponibilidadRepository.existsByUnidadIdUnidad(id))
            return true;
        return false;
    }
}
