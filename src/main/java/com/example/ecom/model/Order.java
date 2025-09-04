package com.example.ecom.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {

    @Id
    private String id; // MongoDB ObjectId

    private String  userId;
    private double totalAmount;
    private LocalDateTime dateTime = LocalDateTime.now();

    // Embedded products in the order
    private List<Product> products;

    public Order() {}

    public Order(String id, String  userId, double totalAmount, LocalDateTime dateTime, List<Product> products) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.dateTime = dateTime;
        this.products = products;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String  getUserId() { return userId; }
    public void setUserId(String  userId) { this.userId = userId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
