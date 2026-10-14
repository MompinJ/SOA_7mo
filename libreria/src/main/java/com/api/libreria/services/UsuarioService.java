package com.api.libreria.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.models.UsuarioModel;
import com.api.libreria.repositories.IUsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    IUsuarioRepository usuarioRepository;


    public ArrayList<UsuarioModel> obtenerUsuarios() {
        return (ArrayList<UsuarioModel>) usuarioRepository.findAll();
    }


    public UsuarioModel obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }



    public UsuarioModel guardarUsuario(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }

    public boolean existeUsuario(Long id) {
        return usuarioRepository.existsById(id);
    }


    public UsuarioModel actualizarUsuario(UsuarioModel request, Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id).orElse(null);
        // Verificar existencia
        if (usuario == null) {
            System.out.println("no se encontro usuario " + id);
            return null;
        }


        usuario.setNombre(request.getNombre());
        usuario.setApellidos(request.getApellidos());
        usuario.setCorreo(request.getCorreo());
        usuario.setRol(request.getRol());
        usuario.setEstado(request.getEstado());
        usuario.setFechaCreacion(request.getFechaCreacion());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        return usuarioRepository.save(usuario);
    }

    public boolean eliminarUsuario(Long id) {
        // Verificar existencia
        if(!usuarioRepository.existsById(id))
        {
            System.out.println("no existe usuario " + id);
            return false;
        }

        usuarioRepository.deleteById(id);
        return true;
    }

    public ArrayList<UsuarioModel> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }
    public ArrayList<UsuarioModel> buscarPorCorreo(String nombre) {
        return usuarioRepository.findByCorreo(nombre);
    }
}
