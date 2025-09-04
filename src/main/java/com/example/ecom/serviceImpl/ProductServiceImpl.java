package com.example.ecom.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.model.Product;
import com.example.ecom.repo.ProductRepository;
import com.example.ecom.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found.");
        }
        return products;
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found matching: " + keyword);
        }
        return products;
    }

    @Override
    public Product addProduct(Product product) {
 
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Product description is required.");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must be non-negative.");
        }
        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("Product quantity must be greater than 0.");
        }

        return productRepository.save(product);
    }
}
