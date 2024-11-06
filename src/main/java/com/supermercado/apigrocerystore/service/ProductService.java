package com.supermercado.apigrocerystore.service;

import java.util.List;

import com.supermercado.apigrocerystore.model.Product;

public interface ProductService {
    
    List<Product> getAll();
    Product getById(Long clientId);
    Product addProduct(Product product);
    Product updateProduct(Long clientId, Product newProduct);
    void deleteProduct(Long clientId);

}
