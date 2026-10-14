package com.api.apartamentos.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.apartamentos.models.ResenaModel;
import com.api.apartamentos.repositories.IResenaRepository;

@Service
public class ResenaService {

    @Autowired
    IResenaRepository resenaRepository;

    public ArrayList<ResenaModel> obtenerResenas() {
        return (ArrayList<ResenaModel>) resenaRepository.findAll();
    }

    public ResenaModel guardarResena(ResenaModel resena) {
        return resenaRepository.save(resena);
    }

    public ResenaModel obtenerPorId(Long id) {
        return resenaRepository.findById(id).orElse(null);
    }

    public void eliminarResena(Long id) {
        resenaRepository.deleteById(id);
    }
}
