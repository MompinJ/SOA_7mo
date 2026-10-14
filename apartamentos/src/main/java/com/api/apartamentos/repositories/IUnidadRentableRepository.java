package com.api.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.apartamentos.models.UnidadRentableModel;

public interface IUnidadRentableRepository extends JpaRepository<UnidadRentableModel, Long> {

    boolean existsByPropiedadIdPropiedad(Long id);

}
