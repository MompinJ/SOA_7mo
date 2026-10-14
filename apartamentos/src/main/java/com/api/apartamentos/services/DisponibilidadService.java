package com.api.apartamentos.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.apartamentos.models.DisponibilidadModel;
import com.api.apartamentos.repositories.IDisponibilidadRepository;

@Service
public class DisponibilidadService {

    @Autowired
    IDisponibilidadRepository disponibilidadRepository;

    public ArrayList<DisponibilidadModel> obtenerDisponibilidades() {
        return (ArrayList<DisponibilidadModel>) disponibilidadRepository.findAll();
    }

    public DisponibilidadModel guardarDisponibilidad(DisponibilidadModel d) {
        return disponibilidadRepository.save(d);
    }

    public DisponibilidadModel obtenerPorId(Long id) {
        return disponibilidadRepository.findById(id).orElse(null);
    }

    public void eliminarDisponibilidad(Long id) {
        disponibilidadRepository.deleteById(id);
    }

}
