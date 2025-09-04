package com.example.ecom.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ecom.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
}
