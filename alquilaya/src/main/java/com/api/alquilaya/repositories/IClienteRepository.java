package com.api.alquilaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.alquilaya.models.ClienteModel;

public interface IClienteRepository extends JpaRepository<ClienteModel, Long> {

    ClienteModel findByEmail(String email);

    ClienteModel findByLicenciaConducir(String licenciaConducir);
    
}
