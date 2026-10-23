package com.universidad.constancias.services;

import com.universidad.constancias.models.Constancia;
import com.universidad.constancias.models.Estudiante;
import com.universidad.constancias.models.Pago;
import com.universidad.constancias.repositories.ConstanciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConstanciaService {

    // logs
    private static final Logger log = LoggerFactory.getLogger(ConstanciaService.class);

    private static final double COSTO_CONSTANCIA = 150.0;

    private final ConstanciaRepository repositorio;
    private final EstudianteService estudianteService;
    private final PagoService pagoService;
    private final NotificacionService notificacionService;

    public ConstanciaService(ConstanciaRepository repositorio, EstudianteService estudianteService,
                             PagoService pagoService, NotificacionService notificacionService) {
        this.repositorio = repositorio;
        this.estudianteService = estudianteService;
        this.pagoService = pagoService;
        this.notificacionService = notificacionService;
    }

    public List<Constancia> listar() {
        return repositorio.findAll();
    }



     // obtenemos la matricula 
    public Constancia solicitarConstancia(String matricula) {
        Estudiante estudiante = estudianteService.consultarEstudiante(matricula);  // se verifica que exista
        if (!estudianteService.verificarInscripcion(estudiante)) {
            return null;  // si no nada
        }
        Pago pago = pagoService.cobrarTramite(estudiante, "Constancia de estudios", COSTO_CONSTANCIA); // se cobra el pago   
        // // OJO, deberiamos de poner una transaccion, pq si falla ps valio
        String archivo = generarConstancia(estudiante);  // se genera la constancia
        notificacionService.enviarCorreo(estudiante, "Tu constancia de estudios esta lista: " + archivo);
        return repositorio.save(new Constancia(estudiante, pago, archivo));
    }

    public Pago generarReferenciaPago(String matricula) {
        Estudiante estudiante = estudianteService.consultarEstudiante(matricula);
        if (!estudianteService.verificarInscripcion(estudiante)) {
            return null;
        }
        return pagoService.generarReferencia(estudiante, "Constancia de estudios", COSTO_CONSTANCIA);
    }

    public String generarConstancia(Estudiante estudiante) {  // con el estudiante
        String archivo = "Constancia_Estudios_" + estudiante.getMatricula() + ".pdf";  // se genera con su matricula
        log.info("[Constancia] PDF generado (simulado): {} para {} ({})", archivo, estudiante.getNombre(), estudiante.getMatricula());
        return archivo;
    }
}