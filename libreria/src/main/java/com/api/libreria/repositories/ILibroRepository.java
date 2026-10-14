package com.api.libreria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.libreria.models.LibroModel;
import java.util.ArrayList;

public interface ILibroRepository extends JpaRepository<LibroModel, Long> {

    ArrayList<LibroModel> findByAutor(String autor);

    ArrayList<LibroModel> findByTitulo(String titulo);
}
