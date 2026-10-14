package com.api.apartamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.apartamentos.models.ReservacionModel;

public interface IReservacionRepository extends JpaRepository<ReservacionModel, Long> {

    boolean existsByClienteId(Long id);
    boolean existsByUnidadIdUnidad(Long id);
    boolean existsByUnidadIdUnidadAndEstado(Long id, String estado);

}
