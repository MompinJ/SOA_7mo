package com.universidad.constancias.repositories;

import com.universidad.constancias.models.Constancia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstanciaRepository extends JpaRepository<Constancia, Long> {
}
