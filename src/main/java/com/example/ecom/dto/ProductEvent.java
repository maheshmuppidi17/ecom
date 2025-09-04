package com.example.ecom.dto;

import com.example.ecom.model.CartItem;

public class ProductEvent {
    private String productId;
    private String name;
    private Double price;
    private Integer quantity;

    public ProductEvent() {}

    // Conversion constructor from CartItem
    public ProductEvent(CartItem item) {
        this.productId = item.getProductId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
    }

    // Getters & Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
