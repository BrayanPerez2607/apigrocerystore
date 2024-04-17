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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.supermercado.apigrocerystore.model.Product;
import com.supermercado.apigrocerystore.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController extends BaseController{
    
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> buscarLibros(@RequestParam(required = false) String codigo){
        if (codigo != null && !codigo.isEmpty()) {
            return productService.getByCodigo(codigo);
        }else{
            return productService.getAll();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> obtenerProductoPorId(@PathVariable Long id){
        Product product = productService.getById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }else{
            return  ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> crearProducto(@RequestBody Product product){
        Product nuevoProdct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProdct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> actualizarLibro(@PathVariable Long id, @RequestBody Product nuevoProdct){
        Product productActualizado = productService.updateProduct(id, nuevoProdct);
        if (productActualizado != null) {
            return ResponseEntity.ok(productActualizado);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
