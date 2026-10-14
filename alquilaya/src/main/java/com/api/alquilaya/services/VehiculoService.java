package com.api.alquilaya.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.IAlquilerRepository;
import com.api.alquilaya.repositories.IVehiculoRepository;

@Service
public class VehiculoService {

    @Autowired
    IVehiculoRepository vehiculoRepository;

    @Autowired
    IAlquilerRepository alquilerRepository;

    public ArrayList<VehiculoModel> obtenerVehiculos() {
        return (ArrayList<VehiculoModel>) vehiculoRepository.findAll();
    }

    public VehiculoModel guardarVehiculo(VehiculoModel vehiculo) {
        if (vehiculo.getEstado() == null) {
            System.out.println("no mandaron estado, se pone DISPONIBLE");
            vehiculo.setEstado("DISPONIBLE");
        }
        return vehiculoRepository.save(vehiculo);
    }

    public boolean tieneAlquileres(Long id) {
        if (alquilerRepository.existsByVehiculoIdVehiculo(id)) {
            return true;
        }
        return false;
    }

    public VehiculoModel obtenerPorId(Long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    public ArrayList<VehiculoModel> buscarPorEstado(String estado) {
        String estadoMayus = estado.toUpperCase();
        return vehiculoRepository.findByEstado(estadoMayus);
    }
    public VehiculoModel buscarPorMatricula(String matricula) {
        ArrayList<VehiculoModel> vehiculos = obtenerVehiculos();
        VehiculoModel encontrado = null;
        for (int i = 0; i < vehiculos.size(); i++) {
            if (vehiculos.get(i).getMatricula().equals(matricula)) {
                encontrado = vehiculos.get(i);
            }
        }
        return encontrado;
    }


    public void eliminarVehiculo(Long id) {
        vehiculoRepository.deleteById(id);
    }
}
