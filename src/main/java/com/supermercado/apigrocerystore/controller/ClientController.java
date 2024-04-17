package com.supermercado.apigrocerystore.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.supermercado.apigrocerystore.model.Client;
import com.supermercado.apigrocerystore.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController extends BaseController{
    
    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAll();
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getById(@PathVariable Long clientId) {
        Client client = clientService.getByClientId(clientId);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Client> creatClient(@RequestBody Client client) {
        Client newClient = clientService.addClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<Client> updateClientById(@PathVariable Long clientId, @RequestBody Client client){
        Client updaClient = clientService.updateClient(clientId, client);
        if (updaClient != null) {
            return ResponseEntity.ok(updaClient);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClientById(@PathVariable Long clientId){
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }

}
