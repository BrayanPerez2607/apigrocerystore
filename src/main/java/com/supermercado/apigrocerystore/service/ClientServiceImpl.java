package com.supermercado.apigrocerystore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermercado.apigrocerystore.model.Client;
import com.supermercado.apigrocerystore.model.Sale;
import com.supermercado.apigrocerystore.repository.ClientRepository;
import com.supermercado.apigrocerystore.repository.SaleRepository;

@Service
public class ClientServiceImpl implements ClientService{
    
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SaleRepository saleRepository;


    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client getByClientId(Long clientId){
        return clientRepository.findById(clientId).orElse(null);
    }

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Long clientId, Client newclient){
        Client existentClient = getByClientId(clientId);
        if (existentClient != null) {
            newclient.setClientId(clientId);
            return clientRepository.save(newclient);
        }
        return null;
    }

    @Transactional
    public void deleteClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        
        // Primero, eliminar las ventas asociadas
        List<Sale> sales = saleRepository.findByClient(client); // Cambiado para usar el objeto Client
        for (Sale sale : sales) {
            saleRepository.delete(sale);
        }
        // Luego, eliminar el cliente
        clientRepository.deleteById(clientId);
    }

}
