
package com.example.ecom.service;

import com.example.ecom.model.Cart;

public interface CartService {
    String addToCart(String userId, String productId, int quantity);
    String removeFromCart(String userId, String productId);
    Cart viewCart(String userId);
    String clearCart(String userId);
}
