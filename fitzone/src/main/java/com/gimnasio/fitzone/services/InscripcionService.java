package com.gimnasio.fitzone.services;

import com.gimnasio.fitzone.models.Clase;
import com.gimnasio.fitzone.models.Inscripcion;
import com.gimnasio.fitzone.models.Socio;
import com.gimnasio.fitzone.repositories.InscripcionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscripcionService {

    private final InscripcionRepository repositorio;
    private final ClaseService claseService;
    private final NotificacionService notificacionService;

    public InscripcionService(InscripcionRepository repositorio, ClaseService claseService,
                              NotificacionService notificacionService) {
        this.repositorio = repositorio;
        this.claseService = claseService;
        this.notificacionService = notificacionService;
    }

    public List<Inscripcion> listar() {
        return repositorio.findAll();
    }

    public boolean yaInscrito(Socio socio, Clase clase) {
        return repositorio.existsBySocioAndClase(socio, clase);
    }

    public Inscripcion inscribirEnClase(Socio socio, Clase clase) {
        claseService.apartarLugar(clase);
        Inscripcion inscripcion = repositorio.save(new Inscripcion(socio, clase));
        notificacionService.enviarConfirmacion(socio, "Quedaste inscrito en " + clase.getNombre() + " (" + clase.getSucursal() + ") el " + clase.getFechaHora());
        notificacionService.programarRecordatorio(socio, "Recuerda tu clase de " + clase.getNombre() + " a las " + clase.getFechaHora().toLocalTime(), clase.getFechaHora().minusHours(2));
        return inscripcion;
    }
}
