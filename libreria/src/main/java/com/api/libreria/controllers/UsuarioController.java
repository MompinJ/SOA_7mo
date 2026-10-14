package com.api.libreria.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.libreria.models.UsuarioModel;
import com.api.libreria.util.Constantes;
import com.api.libreria.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public ArrayList<UsuarioModel> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }
    @GetMapping("/correo")
    public ArrayList<UsuarioModel> buscarPorCorreo(@RequestParam String correo) {
        return usuarioService.buscarPorCorreo(correo);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        UsuarioModel usuario = usuarioService.obtenerPorId(id);

        // Verificar existencia
        if(usuario == null)
        {
            System.out.println("no existe usuario con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarUsuario(@RequestBody UsuarioModel request, @PathVariable("id") Long id) {

        // Verificar existencia
        if (!usuarioService.existeUsuario(id)) {
            System.out.println("no existe usuario con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (request.getNombre() == null || request.getNombre().trim().equals(""))
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);

        if(request.getCorreo()==null || !request.getCorreo().contains("@")) {
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }


        UsuarioModel actualizado = usuarioService.actualizarUsuario(request, id);
        return ResponseEntity.ok(actualizado);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarUsuario(@PathVariable Long id) {
        // Verificar existencia
        if(!usuarioService.existeUsuario(id)) {
            System.out.println("no existe usuario con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }

        usuarioService.eliminarUsuario(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }

    @GetMapping("/nombre")
    public ArrayList<UsuarioModel> buscarPorNombre(@RequestParam String nombre) {
        return usuarioService.buscarPorNombre(nombre);
    }



    @PostMapping
    public ResponseEntity<Object> guardarUsuario(@RequestBody UsuarioModel usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().equals("")) {
            System.out.println("faltan datos");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().trim().equals("")) {
            System.out.println("faltan datos");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        // Verificar q es email
        if(!validarCorreo(usuario.getCorreo())){
            System.out.println("datos invalidos");
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }

        UsuarioModel nuevo = usuarioService.guardarUsuario(usuario);
        System.out.println("----");
        System.out.println("se guardo el usuario con id " + nuevo.getId());
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    private boolean validarCorreo(String correo) {
        if (correo.contains("@")) {
            return true;
        } else {
            return false;
        }
    }
}
