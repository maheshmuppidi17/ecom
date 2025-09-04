package com.example.ecom.serviceImpl;

import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.model.Cart;
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

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId("p1");
        product.setName("Laptop");
        product.setQuantity(5);

        cart = new Cart();
        cart.setId("c1");
        cart.setUserId("u1");
    }

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
    void testAddToCart_InvalidQuantity() {
        when(userRepository.existsById("u1")).thenReturn(true);
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cartService.addToCart("u1", "p1", 0));

        assertEquals("Quantity must be greater than 0.", ex.getMessage());
    }

    @Test
    void testAddToCart_NotEnoughStock() {
        when(userRepository.existsById("u1")).thenReturn(true);
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cartService.addToCart("u1", "p1", 10));

        assertEquals("Not enough stock for product: Laptop", ex.getMessage());
    }

    @Test
    void testAddToCart_Success() {
        when(userRepository.existsById("u1")).thenReturn(true);
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId("u1")).thenReturn(Optional.of(cart));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        String result = cartService.addToCart("u1", "p1", 2);

        assertEquals("Added 2 x Laptop to cart.", result);
        verify(cartRepository).save(cart);
        verify(productRepository).save(product);
        assertEquals(3, product.getQuantity());
    }

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
        verify(cartRepository).save(cart);
    }

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

    @Test
    void testClearCart_Unimplemented() {
        // Since method returns null, this will pass only if it's null
        assertNull(cartService.clearCart("u1"));
    }
}
