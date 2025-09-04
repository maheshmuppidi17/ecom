package com.example.ecom.dto;

import com.example.ecom.model.CartItem;

public class ProductEvent {
    private String id;
    private String name;
    private Double price;
    private String description;
    private Integer quantity;

    public ProductEvent() {}

    // Conversion constructor from CartItem
    public ProductEvent(CartItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.description=item.getDescription();
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
