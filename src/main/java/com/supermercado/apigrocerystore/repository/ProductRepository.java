package com.supermercado.apigrocerystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermercado.apigrocerystore.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
