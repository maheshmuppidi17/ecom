package com.example.ecom.service;

import java.util.List;

import com.example.ecom.model.Product;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> searchProducts(String keyword);
}