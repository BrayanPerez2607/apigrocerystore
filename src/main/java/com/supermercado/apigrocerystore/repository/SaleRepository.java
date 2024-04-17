package com.supermercado.apigrocerystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermercado.apigrocerystore.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long>{
    
}
