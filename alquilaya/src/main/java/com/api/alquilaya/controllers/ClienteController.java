package com.api.alquilaya.controllers;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.services.ClienteService;
import com.api.alquilaya.util.Constantes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ArrayList<ClienteModel> obtenerClientes() {
        return clienteService.obtenerClientes();
    }

    @PostMapping
    public ResponseEntity<Object> guardarCliente(@RequestBody ClienteModel cliente) {
        // Validaciones
        if (cliente.getNombre() == null || cliente.getApellido() == null || cliente.getLicenciaConducir() == null) {
            System.out.println("faltan datos del cliente");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (cliente.getEmail() == null)
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);

        // Verificar q es email
        if (esEmailValido(cliente.getEmail()) == false) {
            System.out.println("correo invalido: " + cliente.getEmail());
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }
        if (clienteService.buscarPorEmail(cliente.getEmail()) != null || clienteService.buscarPorLicencia(cliente.getLicenciaConducir()) != null) {
            System.out.println("ya hay un cliente con ese correo o licencia");
            return Constantes.responder(Constantes.ERROR_DUPLICADO);
        }

        ClienteModel nuevo = clienteService.guardarCliente(cliente);
        System.out.println("----");
        System.out.println("se guardo el cliente con id " + nuevo.getIdCliente());
        return Constantes.responder(Constantes.CREADO, nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarCliente(@RequestBody ClienteModel request, @PathVariable("id") Long id) {
        if(clienteService.obtenerPorId(id) == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        System.out.println("actualizando cliente " + id);
        if (request.getNombre() == null || request.getApellido() == null || request.getLicenciaConducir() == null || request.getEmail() == null || !request.getEmail().contains("@")) {
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }
        ClienteModel otro = clienteService.buscarPorEmail(request.getEmail());
        if (otro != null && !otro.getIdCliente().equals(id)) {
            System.out.println("el correo ya lo tiene el cliente " + otro.getIdCliente());
            return Constantes.responder(Constantes.ERROR_DUPLICADO);
        }
        otro = clienteService.buscarPorLicencia(request.getLicenciaConducir());
        if (otro != null && !otro.getIdCliente().equals(id)) {
            System.out.println("la licencia ya la tiene el cliente " + otro.getIdCliente());
            return Constantes.responder(Constantes.ERROR_DUPLICADO);
        }
        request.setIdCliente(id);
        return Constantes.responder(Constantes.ACTUALIZADO, clienteService.guardarCliente(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        ClienteModel cliente = clienteService.obtenerPorId(id);
        if (cliente == null) {
            System.out.println("no existe cliente con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(cliente);
    }
    

    @GetMapping("/licencia/{licencia}")
    public ResponseEntity<Object> buscarPorLicencia(@PathVariable String licencia) {
        ClienteModel cliente = clienteService.buscarPorLicencia(licencia);
        // Verificar existencia
        if (cliente == null) {
            System.out.println("no hay cliente con la licencia " + licencia);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(cliente);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Object> buscarPorEmail(@PathVariable String email) {
        System.out.println("buscando " + email);
        ClienteModel cliente = clienteService.buscarPorEmail(email);
        if (cliente == null)
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarCliente(@PathVariable Long id) {
        if (!clienteService.existeCliente(id)) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (clienteService.tieneAlquileres(id)) {
            System.out.println("no se puede borrar, el cliente tiene alquileres");
            return Constantes.responder(Constantes.ERROR_TIENE_RELACIONES);
        }
        clienteService.eliminarCliente(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }

    private boolean esEmailValido(String email) {
        boolean valido = false;
        if (email.contains("@")) {
            valido = true;
        }
        return valido;
    }
}
