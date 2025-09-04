package com.example.ecom.serviceImpl;

import com.example.ecom.model.Product;
import com.example.ecom.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product1 = new Product();
        product1.setId("prod1");
        product1.setName("Laptop");
        product1.setPrice(1000.0);

        product2 = new Product();
        product2.setId("prod2");
        product2.setName("Phone");
        product2.setPrice(500.0);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals("Phone", result.get(1).getName());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testSearchProducts() {
        String keyword = "Laptop";
        List<Product> products = Arrays.asList(product1);
        when(productRepository.findByNameContainingIgnoreCase(keyword)).thenReturn(products);

        List<Product> result = productService.searchProducts(keyword);

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());

        verify(productRepository, times(1)).findByNameContainingIgnoreCase(keyword);
    }
}
