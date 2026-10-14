package com.api.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.apartamentos.models.PagoModel;

public interface IPagoRepository extends JpaRepository<PagoModel, Long> {

    boolean existsByReservaIdReservacion(Long id);

}
