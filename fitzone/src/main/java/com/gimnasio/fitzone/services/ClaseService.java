package com.gimnasio.fitzone.services;

import com.gimnasio.fitzone.models.Clase;
import com.gimnasio.fitzone.repositories.ClaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseService {

    private final ClaseRepository repositorio;

    public ClaseService(ClaseRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Clase> consultarClases() {
        return repositorio.findAll();
    }

    public Clase consultarCupo(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    public Clase apartarLugar(Clase clase) {
        clase.setInscritos(clase.getInscritos() + 1);
        return repositorio.save(clase);
    }
}
