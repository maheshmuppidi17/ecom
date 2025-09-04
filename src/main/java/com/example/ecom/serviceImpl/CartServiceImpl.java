package com.example.ecom.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.model.Cart;
import com.example.ecom.model.Product;
import com.example.ecom.repo.CartRepository;
import com.example.ecom.repo.ProductRepository;
import com.example.ecom.repo.UserRepository;
import com.example.ecom.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String addToCart(String userId, String productId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        // Verify product exists
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        // Find or create cart
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        if (cart.getUserId() == null) {
            cart.setUserId(userId);
        }

        cart.addProduct(product);
        cartRepository.save(cart);

        return "Product added to cart.";
    }



    @Override
    public String removeFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));

        cart.removeProduct(productId);
        cartRepository.save(cart);
        return "Product removed from cart.";
    }

    @Override
    public Cart viewCart(String userId) {
        return cartRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));
    }
}
