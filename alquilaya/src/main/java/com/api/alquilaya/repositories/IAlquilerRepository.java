package com.api.alquilaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.alquilaya.models.AlquilerModel;

public interface IAlquilerRepository extends JpaRepository<AlquilerModel, Long> {

    boolean existsByClienteIdCliente(Long id);
    boolean existsByVehiculoIdVehiculo(Long id);

}
