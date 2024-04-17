package com.supermercado.apigrocerystore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermercado.apigrocerystore.model.Product;
import com.supermercado.apigrocerystore.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{
    
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("The product with ID " + id + " doesn't exist."));
    }

    @Override
    public List<Product> getByCodigo(String codigo){
        return productRepository.findByCodigo(codigo);
    }

    @Override
    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product newProduct){
        if (productRepository.existsById(null)) {
            newProduct.setId(id);
            return productRepository.save(newProduct);
        }else{
            throw new IllegalArgumentException("The product with ID " + id + " doesn't exist.");
        }
    }

    @Override
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

}
