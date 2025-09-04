package com.example.ecom.controller;

import com.example.ecom.contrller.ProductController;
import com.example.ecom.model.Product;
import com.example.ecom.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setId("prod1");
        product1.setName("Laptop");

        Product product2 = new Product();
        product2.setId("prod2");
        product2.setName("Phone");

        List<Product> mockProducts = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(mockProducts);

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(2, response.getBody().size());
        assertEquals(mockProducts, response.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testSearchProducts() {
        String keyword = "Laptop";

        Product product = new Product();
        product.setId("prod1");
        product.setName("Laptop");

        List<Product> mockProducts = Arrays.asList(product);

        when(productService.searchProducts(keyword)).thenReturn(mockProducts);

        ResponseEntity<List<Product>> response = productController.search(keyword);

        assertEquals(1, response.getBody().size());
        assertEquals(mockProducts, response.getBody());
        verify(productService, times(1)).searchProducts(keyword);
    }
}
