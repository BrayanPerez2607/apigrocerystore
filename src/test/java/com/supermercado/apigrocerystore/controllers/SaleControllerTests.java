package com.supermercado.apigrocerystore.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.supermercado.apigrocerystore.controller.SaleController;
import com.supermercado.apigrocerystore.model.Client;
import com.supermercado.apigrocerystore.model.Product;
import com.supermercado.apigrocerystore.model.Sale;
import com.supermercado.apigrocerystore.service.SaleService;

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
@WebMvcTest(SaleController.class)
public class SaleControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleService saleService;

    @InjectMocks
    private SaleController saleController;

    private Sale sale1;
    private Sale sale2;
    private List<Sale> saleList;
    private Client client;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(saleController).build();

        client = new Client(1L, "John Doe", "johndoe@example.com", "3017580088", "Carrera 82A#104 DD 39", "JavaDeveloper2015", 1018226759L);
        product1 = new Product(1L, "Product1", 10000, "Almojabana", "Parva", "800g", "Natipan", "Pan sabroso", "null");
        product2 = new Product(2L, "Product2", 8000, "Pan Perro", "Parva", "500g", "Natipan", "El mejor pan perro", "null");

        sale1 = new Sale(1L, client, Arrays.asList(product1, product2), new BigDecimal("100.00"), LocalDateTime.now());
        sale2 = new Sale(2L, client, Arrays.asList(product1), new BigDecimal("50.00"), LocalDateTime.now());
        saleList = Arrays.asList(sale1, sale2);
    }

    @Test
    public void testCreateSale() throws Exception {
        when(saleService.createSale(anyLong(), anyList(), any(BigDecimal.class))).thenReturn(sale1);

        mockMvc.perform(post("/apigrocerystore/sales")
                        .param("clientId", "1")
                        .param("productIds", "1,2")
                        .param("total", "100.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sale1.getId()));
    }

    @Test
    public void testFindSaleById_Success() throws Exception {
        when(saleService.findSaleById(anyLong())).thenReturn(sale1);

        mockMvc.perform(get("/apigrocerystore/sales/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sale1.getId()));
    }

    /*
     * En esta prueba (testFindSaleById_NotFound) no sale en si un error, si no que se espera
     * otra respuesta en el status esto se podira arreglar programando de manera mas adecuada
     * este endpoint en el controlador especifico
     * */
    @Test
    public void testFindSaleById_NotFound() throws Exception {
        when(saleService.findSaleById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/apigrocerystore/sales/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindAllSales() throws Exception {
        when(saleService.findAllSales()).thenReturn(saleList);

        mockMvc.perform(get("/apigrocerystore/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sale1.getId()))
                .andExpect(jsonPath("$[1].id").value(sale2.getId()));
    }

    /*
    * En esta prueba (testDeleteSaleById) no sale en si un error, si no que se espera otra
    * respuesta en el status esto se podira arreglar programando de manera mas adecuada
    * este endpoint en el controlador especifico
    * */
    @Test
    public void testDeleteSaleById() throws Exception {
        mockMvc.perform(delete("/apigrocerystore/sales/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateSale_Success() throws Exception {
        when(saleService.updateSale(anyLong(), any(Sale.class))).thenReturn(sale1);

        mockMvc.perform(put("/apigrocerystore/sales/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sale1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sale1.getId()));
    }

    @Test
    public void testUpdateSale_NotFound() throws Exception {
        when(saleService.updateSale(anyLong(), any(Sale.class))).thenReturn(null);

        mockMvc.perform(put("/apigrocerystore/sales/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sale1)))
                .andExpect(status().isNotFound());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

