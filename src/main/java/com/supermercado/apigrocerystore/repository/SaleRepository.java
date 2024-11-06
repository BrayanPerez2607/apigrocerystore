package com.supermercado.apigrocerystore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermercado.apigrocerystore.model.Client;
import com.supermercado.apigrocerystore.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long>{
    List<Sale> findByClient(Client client); // Cambiado a Client
}
