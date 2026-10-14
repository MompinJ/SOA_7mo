package com.api.apartamentos.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.apartamentos.models.PropiedadImagenModel;
import com.api.apartamentos.repositories.IPropiedadImagenRepository;

@Service
public class PropiedadImagenService {
    @Autowired
    IPropiedadImagenRepository imagenRepository;

    public ArrayList<PropiedadImagenModel> obtenerImagenes() {
        return (ArrayList<PropiedadImagenModel>) imagenRepository.findAll();
    }
    public PropiedadImagenModel guardarImagen(PropiedadImagenModel imagen) {
        return imagenRepository.save(imagen);
    }
    public PropiedadImagenModel obtenerPorId(Long id) {
        return imagenRepository.findById(id).orElse(null);
    }
    public void eliminarImagen(Long id) {
        imagenRepository.deleteById(id);
    }
}
