package com.api.alquilaya.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.repositories.IOficinaAlquilerRepository;
import com.api.alquilaya.repositories.IVehiculoRepository;

@Service
public class OficinaAlquilerService {

    @Autowired
    IOficinaAlquilerRepository oficinaRepository;
    @Autowired
    IVehiculoRepository vehiculoRepository;

    public boolean tieneVehiculos(Long id) {
        return vehiculoRepository.existsByOficinaIdOficina(id);
    }

    public ArrayList<OficinaAlquilerModel> obtenerOficinas() {
        return (ArrayList<OficinaAlquilerModel>) oficinaRepository.findAll();
    }

    public OficinaAlquilerModel guardarOficina(OficinaAlquilerModel oficina) {
        return oficinaRepository.save(oficina);
    }

    public OficinaAlquilerModel obtenerPorId(Long id) {
        return oficinaRepository.findById(id).orElse(null);
    }


    public void eliminarOficina(Long id) {
        oficinaRepository.deleteById(id);
    }
}
