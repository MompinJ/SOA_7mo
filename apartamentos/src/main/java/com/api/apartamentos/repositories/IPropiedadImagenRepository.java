package com.api.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.apartamentos.models.PropiedadImagenModel;

public interface IPropiedadImagenRepository extends JpaRepository<PropiedadImagenModel, Long> {

    boolean existsByPropiedadIdPropiedad(Long id);

}
