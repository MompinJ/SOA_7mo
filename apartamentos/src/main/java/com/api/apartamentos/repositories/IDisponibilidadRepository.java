package com.api.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.apartamentos.models.DisponibilidadModel;

public interface IDisponibilidadRepository extends JpaRepository<DisponibilidadModel, Long> {

    boolean existsByUnidadIdUnidad(Long id);

}
