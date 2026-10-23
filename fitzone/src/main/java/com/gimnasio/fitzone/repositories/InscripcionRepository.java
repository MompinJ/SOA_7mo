package com.gimnasio.fitzone.repositories;

import com.gimnasio.fitzone.models.Clase;
import com.gimnasio.fitzone.models.Inscripcion;
import com.gimnasio.fitzone.models.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsBySocioAndClase(Socio socio, Clase clase);
}
