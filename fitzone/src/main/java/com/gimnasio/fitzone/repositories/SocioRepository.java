package com.gimnasio.fitzone.repositories;

import com.gimnasio.fitzone.models.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocioRepository extends JpaRepository<Socio, String> {
}
