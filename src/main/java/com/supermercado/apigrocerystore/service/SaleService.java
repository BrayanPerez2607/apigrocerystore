package com.supermercado.apigrocerystore.service;

import java.math.BigDecimal;
import java.util.List;

import com.supermercado.apigrocerystore.model.Sale;

public interface SaleService {

    Sale createSale(Long clientId, List<Long> productIds, BigDecimal total);

    Sale findSaleById(Long id);

    List<Sale> findAllSales();

    void deleteSaleById(Long id);

    Sale updateSale(Long id, Sale sale);

}
