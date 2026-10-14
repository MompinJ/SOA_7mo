package com.api.libreria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.libreria.models.PrestamoModel;
import java.util.ArrayList;

public interface IPrestamoRepository extends JpaRepository<PrestamoModel, Long> {

    ArrayList<PrestamoModel> findByIsbn(Long isbn);
}
