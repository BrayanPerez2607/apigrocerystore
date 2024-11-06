package com.supermercado.apigrocerystore.controller;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateSaleRequest {
    private Long clientId;
    private List<Long> productIds;
    private BigDecimal total;
}
