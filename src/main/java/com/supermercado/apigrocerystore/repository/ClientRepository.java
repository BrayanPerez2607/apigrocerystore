package com.supermercado.apigrocerystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermercado.apigrocerystore.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
    
}
