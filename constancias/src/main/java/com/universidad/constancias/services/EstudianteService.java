package com.universidad.constancias.services;

import com.universidad.constancias.models.Estudiante;
import com.universidad.constancias.repositories.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository repositorio;

    public EstudianteService(EstudianteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Estudiante> listar() {
        return repositorio.findAll();
    }

    public Estudiante consultarEstudiante(String matricula) {
        return repositorio.findById(matricula).orElse(null);
    }

    public boolean verificarInscripcion(Estudiante estudiante) {
        return estudiante != null && estudiante.isActivo();
    }
}
