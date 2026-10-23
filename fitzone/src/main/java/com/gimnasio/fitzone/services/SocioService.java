package com.gimnasio.fitzone.services;

import com.gimnasio.fitzone.models.Socio;
import com.gimnasio.fitzone.repositories.SocioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SocioService {

    private final SocioRepository repositorio;

    public SocioService(SocioRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Socio> listar() {
        return repositorio.findAll();
    }

    public Socio consultarSocio(String numeroSocio) {
        return repositorio.findById(numeroSocio).orElse(null);
    }

    public boolean consultarMembresia(Socio socio, LocalDate fecha) {
        return !socio.getVencimientoMembresia().isBefore(fecha);
    }
}
