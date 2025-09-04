package com.example.ecom.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cart {
    private String id;
    private String userId;
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}

    public Cart(String userId) {
        this.userId = userId;
    }

    // Add product to cart
    public void addProduct(Product product, int quantity) {
        // Check if already exists
        for (CartItem item : items) {
            if (item.getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Add new item
        CartItem newItem = new CartItem(product.getId(), product.getName(),product.getDescription() , product.getPrice(), quantity);
        items.add(newItem);
    }

    // Remove product from cart
    public void removeProduct(String productId) {
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getId().equals(productId)) {
                iterator.remove();
                break;
            }
        }
    }

    // Calculate total price
    public double calculateTotal() {
        return items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    // Clear cart
    public void clear() {
        items.clear();
    }

    // Getters/Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
