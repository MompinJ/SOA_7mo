package com.universidad.constancias.repositories;

import com.universidad.constancias.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Pago findByReferencia(String referencia);
}
