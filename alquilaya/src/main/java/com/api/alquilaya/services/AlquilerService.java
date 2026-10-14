package com.api.alquilaya.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.repositories.IAlquilerRepository;

@Service
public class AlquilerService {

    @Autowired
    IAlquilerRepository alquilerRepository;

    public ArrayList<AlquilerModel> obtenerAlquileres() {
        return (ArrayList<AlquilerModel>) alquilerRepository.findAll();
    }

    public AlquilerModel obtenerPorId(Long id) {
        return alquilerRepository.findById(id).orElse(null);
    }

    public AlquilerModel guardarAlquiler(AlquilerModel alquiler) {
        return alquilerRepository.save(alquiler);
    }


    public void eliminarAlquiler(Long id) {
        System.out.println("borrando alquiler " + id);
        alquilerRepository.deleteById(id);
    }
}
