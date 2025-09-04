package com.example.ecom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
public class Cart {

    @Id
    private String id; // MongoDB ObjectId

    private String userId; // Link cart to user

    private List<Product> products = new ArrayList<>();

    // Constructors
    public Cart() {}

    public Cart(String userId) {
        this.userId = userId;
    }

    // Add a product to cart
    public void addProduct(Product product) {
        products.add(product);
    }

    // Remove product by productId
    public void removeProduct(String productId) {
        products.removeIf(p -> p.getId().equals(productId));
    }

    // Clear all products
    public void clear() {
        products.clear();
    }

    // Calculate total price (taking quantity into account)
    public double calculateTotal() {
        return products.stream()
                       .mapToDouble(p -> p.getPrice() * p.getQuantity())
                       .sum();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
