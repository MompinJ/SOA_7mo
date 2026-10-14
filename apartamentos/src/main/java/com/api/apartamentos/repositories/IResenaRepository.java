package com.api.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.apartamentos.models.ResenaModel;

public interface IResenaRepository extends JpaRepository<ResenaModel, Long> {

    boolean existsByReservaIdReservacion(Long id);

}
