package com.example.ecom.serviceImpl;

import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.model.Cart;
import com.example.ecom.model.CartItem;
import com.example.ecom.model.Product;
import com.example.ecom.repo.CartRepository;
import com.example.ecom.repo.ProductRepository;
import com.example.ecom.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Product product;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId("p1");
        product.setName("Laptop");
        product.setQuantity(5);

        cartItem = new CartItem("p1", "Laptop", "Description", 1000.0, 2);

        cart = new Cart();
        cart.setId("c1");
        cart.setUserId("u1");
        cart.getItems().add(cartItem);
    }

    // ---------------- addToCart ----------------

    @Test
    void testAddToCart_UserNotFound() {
        when(userRepository.existsById("u1")).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart("u1", "p1", 1));

        assertEquals("User not found with ID: u1", ex.getMessage());
    }

    @Test
    void testAddToCart_ProductNotFound() {
        when(userRepository.existsById("u1")).thenReturn(true);
        when(productRepository.findById("p1")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart("u1", "p1", 1));

        assertEquals("Product not found with ID: p1", ex.getMessage());
    }

    @Test
    void testAddToCart_NotEnoughStock() {
        when(userRepository.existsById("u1")).thenReturn(true);
        product.setQuantity(1);
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cartService.addToCart("u1", "p1", 2));

        assertEquals("Not enough stock for product: Laptop", ex.getMessage());
    }

    @Test
    void testAddToCart_Success_NewCart() {
        when(userRepository.existsById("u1")).thenReturn(true);
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId("u1")).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        String result = cartService.addToCart("u1", "p1", 2);

        assertEquals("Added 2 x Laptop to cart.", result);
        assertEquals(3, product.getQuantity());
        verify(cartRepository).save(any(Cart.class));
        verify(productRepository).save(product);
    }

    // ---------------- removeFromCart ----------------

    @Test
    void testRemoveFromCart_CartNotFound() {
        when(cartRepository.findByUserId("u1")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeFromCart("u1", "p1"));

        assertEquals("Cart not found for user ID: u1", ex.getMessage());
    }

    @Test
    void testRemoveFromCart_Success() {
        when(cartRepository.findByUserId("u1")).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        String result = cartService.removeFromCart("u1", "p1");

        assertEquals("Product removed from cart.", result);
        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    // ---------------- viewCart ----------------

    @Test
    void testViewCart_CartNotFound() {
        when(cartRepository.findByUserId("u1")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> cartService.viewCart("u1"));

        assertEquals("Cart not found for user ID: u1", ex.getMessage());
    }

    @Test
    void testViewCart_Success() {
        when(cartRepository.findByUserId("u1")).thenReturn(Optional.of(cart));

        Cart foundCart = cartService.viewCart("u1");

        assertEquals(cart, foundCart);
    }

    // ---------------- clearCart ----------------

    @Test
    void testClearCart_CartNotFound() {
        when(cartRepository.findByUserId("u1")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> cartService.clearCart("u1"));

        assertEquals("Cart not found for user ID: u1", ex.getMessage());
    }

    @Test
    void testClearCart_Success() {
        when(cartRepository.findByUserId("u1")).thenReturn(Optional.of(cart));
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(cartRepository.save(cart)).thenReturn(cart);

        String result = cartService.clearCart("u1");

        assertEquals("Cart cleared successfully.", result);
        assertTrue(cart.getItems().isEmpty());
        assertEquals(7, product.getQuantity()); // original 5 + 2 from cartItem
        verify(cartRepository).save(cart);
        verify(productRepository).save(product);
    }
}
