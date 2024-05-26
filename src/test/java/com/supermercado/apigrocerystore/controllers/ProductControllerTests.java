package com.supermercado.apigrocerystore.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import com.supermercado.apigrocerystore.controller.ProductController;
import com.supermercado.apigrocerystore.model.Product;
import com.supermercado.apigrocerystore.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;
    private List<Product> productList;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        product1 = new Product(1L, "Product1", 10000, "Almojabana", "Parva", "800g", "Natipan", "Pan sabroso", "null");
        product2 = new Product(2L, "Product2", 8000, "Pan Perro", "Parva", "500g", "Natipan", "El mejor pan perro", "null");
        productList = Arrays.asList(product1, product2);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.getAll()).thenReturn(productList);

        mockMvc.perform(get("/apigrocerystore/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product1.getId()))
                .andExpect(jsonPath("$[1].id").value(product2.getId()));
    }

    @Test
    public void testGetProductById_Success() throws Exception {
        when(productService.getById(anyLong())).thenReturn(product1);

        mockMvc.perform(get("/apigrocerystore/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product1.getId()));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(productService.getById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/apigrocerystore/products/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productService.addProduct(any(Product.class))).thenReturn(product1);

        mockMvc.perform(post("/apigrocerystore/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(product1.getId()));
    }

    @Test
    public void testUpdateProductById_Success() throws Exception {
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product1);

        mockMvc.perform(put("/apigrocerystore/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product1.getId()));
    }

    @Test
    public void testUpdateProductById_NotFound() throws Exception {
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(null);

        mockMvc.perform(put("/apigrocerystore/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteProductById() throws Exception {
        mockMvc.perform(delete("/apigrocerystore/products/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
