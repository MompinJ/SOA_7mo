package com.api.libreria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.libreria.models.UsuarioModel;
import java.util.ArrayList;

public interface IUsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    ArrayList<UsuarioModel> findByNombre(String nombre);

    ArrayList<UsuarioModel> findByCorreo(String correo);
}
