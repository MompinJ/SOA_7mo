package com.api.apartamentos.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.apartamentos.models.ClienteModel;
import com.api.apartamentos.services.ClienteService;
import com.api.apartamentos.util.Constantes;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ArrayList<ClienteModel> obtenerClientes() {
        System.out.println("total de clientes: " + clienteService.obtenerClientes().size());
        return clienteService.obtenerClientes();
    }

    @PostMapping
    public ResponseEntity<Object> guardarCliente(@RequestBody ClienteModel cliente) {
        // Validaciones
        if (cliente.getNombres() == null || cliente.getNombres().trim().equals("")) {
            System.out.println("faltan datos del cliente");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().equals(""))
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);

        // Verificar q es email
        if(!cliente.getEmail().contains("@")){
            System.out.println("correo invalido: " + cliente.getEmail());
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }

        ClienteModel nuevo = clienteService.guardarCliente(cliente);
        System.out.println("----");
        System.out.println("se guardo el cliente con id " + nuevo.getId());
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarCliente(@RequestBody ClienteModel request, @PathVariable("id") Long id) {
        if (request.getNombres() == null || request.getEmail() == null || !request.getEmail().contains("@")) {
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }
        ClienteModel actualizado = clienteService.actualizarCliente(request, id);
        // Verificar existencia
        if (actualizado == null) {
            System.out.println("no existe cliente con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(actualizado);
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


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarCliente(@PathVariable Long id) {
        if(clienteService.obtenerPorId(id) == null) {
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        if (clienteService.tieneRelaciones(id)) {
            System.out.println("no se puede borrar, el cliente tiene reservaciones o propiedades");
            return Constantes.responder(Constantes.ERROR_TIENE_RELACIONES);
        }
        clienteService.eliminarCliente(id);
        return Constantes.responder(Constantes.ELIMINADO);
    }
}
