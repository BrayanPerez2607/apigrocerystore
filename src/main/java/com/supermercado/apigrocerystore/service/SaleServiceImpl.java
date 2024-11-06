package com.supermercado.apigrocerystore.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermercado.apigrocerystore.model.Client;
import com.supermercado.apigrocerystore.model.Product;
import com.supermercado.apigrocerystore.model.Sale;
import com.supermercado.apigrocerystore.repository.ClientRepository;
import com.supermercado.apigrocerystore.repository.ProductRepository;
import com.supermercado.apigrocerystore.repository.SaleRepository;

import jakarta.transaction.Transactional;

@Service
public class SaleServiceImpl implements SaleService{
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Sale createSale(Long clientId, List<Long> productIds, BigDecimal total) {
        // Crea una nueva instancia de Sale
        Sale sale = new Sale();
        sale.setTotal(total);
        sale.setPurchaseDate(LocalDateTime.now());

        // Establece el usuario que realizó la compra
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("We can't find the client"));
        sale.setClient(client);
        // Establece los productos que se compraron
        List<Product> products = productRepository.findAllById(productIds);
        sale.setProducts(products);

        // Guarda la venta en la base de datos
        return saleRepository.save(sale);
    }

    @Override
    public Sale findSaleById(Long id) {
        // Busca una venta en la base de datos por su identificador
        return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    @Override
    public List<Sale> findAllSales() {
        // Busca todas las ventas en la base de datos
        return saleRepository.findAll();
    }

    @Override
    public void deleteSaleById(Long id) {
        // Elimina una venta de la base de datos por su identificador
        saleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Sale updateSale(Long id, Sale sale) {
        // Busca una venta en la base de datos por su identificador
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // Actualiza los atributos de la venta
        existingSale.setTotal(sale.getTotal());

        // Establece el cliente
        if (sale.getClient() != null && sale.getClient().getId() != null) {
            Client client = clientRepository.findById(sale.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            existingSale.setClient(client);
        }

        // Establece los productos
        if (sale.getProducts() != null) {
            List<Product> products = productRepository.findAllById(
                    sale.getProducts().stream()
                            .map(Product::getId)
                            .collect(Collectors.toList())
            );
            existingSale.setProducts(products);
        }

        // Guarda los cambios en la base de datos
        return saleRepository.save(existingSale);
    }

}
