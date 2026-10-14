package com.api.apartamentos.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.apartamentos.models.PropiedadModel;
import com.api.apartamentos.repositories.IPropiedadRepository;
import com.api.apartamentos.repositories.IUnidadRentableRepository;
import com.api.apartamentos.repositories.IPropiedadImagenRepository;

@Service
public class PropiedadService {

    @Autowired
    IPropiedadRepository propiedadRepository;
    @Autowired
    IUnidadRentableRepository unidadRepository;
    @Autowired
    IPropiedadImagenRepository imagenRepository;

    public ArrayList<PropiedadModel> obtenerPropiedades() {
        return (ArrayList<PropiedadModel>) propiedadRepository.findAll();
    }

    public PropiedadModel guardarPropiedad(PropiedadModel propiedad) {
        if (propiedad.getEstado() == null) {
            propiedad.setEstado("DISPONIBLE");
        }
        return propiedadRepository.save(propiedad);
    }

    public PropiedadModel obtenerPorId(Long id) {
        return propiedadRepository.findById(id).orElse(null);
    }

    public void eliminarPropiedad(Long id) {
        propiedadRepository.deleteById(id);
    }

    public ArrayList<PropiedadModel> buscarPorTipo(String tipo) {
        ArrayList<PropiedadModel> resultado = new ArrayList<>();
        for (PropiedadModel p : obtenerPropiedades()) {
            if (p.getTipo() != null && p.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(p);
            }
        }
        System.out.println("propiedades de tipo " + tipo + ": " + resultado.size());
        return resultado;
    }

    public boolean tieneRelaciones(Long id) {
        return unidadRepository.existsByPropiedadIdPropiedad(id) || imagenRepository.existsByPropiedadIdPropiedad(id);
    }
}
