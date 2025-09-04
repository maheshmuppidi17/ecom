package com.example.ecom.dto;

import com.example.ecom.model.Order;
import com.example.ecom.model.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderEvent {
    private String id;
    private String userId;
    private List<ProductEvent> products;
    private Double totalAmount;
    private LocalDateTime dateTime;
    private String status;

    // Default constructor
    public OrderEvent() {}

    // Conversion constructor from Order
    public OrderEvent(Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.totalAmount = order.getTotalAmount();
        this.dateTime = order.getDateTime();
        this.status = "CREATED";
        
        // Convert Product objects to ProductEvent objects
        if (order.getProducts() != null) {
            this.products = order.getProducts().stream()
                .map(ProductEvent::new)
                .collect(Collectors.toList());
        }
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<ProductEvent> getProducts() { return products; }
    public void setProducts(List<ProductEvent> products) { this.products = products; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}