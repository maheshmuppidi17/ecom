package com.example.ecom.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.model.Cart;
import com.example.ecom.model.CartItem;
import com.example.ecom.model.Product;
import com.example.ecom.repo.CartRepository;
import com.example.ecom.repo.ProductRepository;
import com.example.ecom.repo.UserRepository;

import com.example.ecom.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    @Override
    public String addToCart(String userId, String productId, int quantity) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        if (cart.getUserId() == null) {
            cart.setUserId(userId);
        }

        cart.addProduct(product, quantity);
        cartRepository.save(cart);

        return "Added " + quantity + " x " + product.getName() + " to cart.";
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

    @Override
    public String clearCart(String userId) {
        // Find cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));

        // Restore stock for all products
        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + item.getId()));
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        // Clear cart items
        cart.getItems().clear();
        cartRepository.save(cart);

        return "Cart cleared successfully.";
    }


}
