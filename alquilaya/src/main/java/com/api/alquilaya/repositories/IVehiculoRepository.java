package com.api.alquilaya.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.alquilaya.models.VehiculoModel;

public interface IVehiculoRepository extends JpaRepository<VehiculoModel, Long> {

    ArrayList<VehiculoModel> findByEstado(String estado);

    boolean existsByOficinaIdOficina(Long id);

}
