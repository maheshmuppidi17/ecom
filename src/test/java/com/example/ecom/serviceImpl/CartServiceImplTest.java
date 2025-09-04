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
public class CartServiceImplTest {

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
    void setUp() {
        product = new Product();
        product.setId("testProductId");
        product.setName("Laptop");
        product.setPrice(999.99);

        cart = new Cart();
        cart.setId("testCartId");
        cart.setUserId("testUserId");
    }

    @Test
    void testAddToCart_NewCart() {
        // Arrange
        when(userRepository.existsById("testUserId")).thenReturn(true);
        when(productRepository.findById("testProductId")).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart savedCart = invocation.getArgument(0);
            savedCart.setId("savedCartId"); // Simulate saving
            return savedCart;
        });

        // Act
        String result = cartService.addToCart("testUserId", "testProductId");

        // Assert
        assertEquals("Product added to cart.", result);
        verify(userRepository, times(1)).existsById("testUserId");
        verify(productRepository, times(1)).findById("testProductId");
        verify(cartRepository, times(1)).findByUserId("testUserId");
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddToCart_ExistingCart() {
        // Arrange
        when(userRepository.existsById("testUserId")).thenReturn(true);
        when(productRepository.findById("testProductId")).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        // Act
        String result = cartService.addToCart("testUserId", "testProductId");

        // Assert
        assertEquals("Product added to cart.", result);
        verify(userRepository, times(1)).existsById("testUserId");
        verify(productRepository, times(1)).findById("testProductId");
        verify(cartRepository, times(1)).findByUserId("testUserId");
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testAddToCart_UserNotFound() {
        // Arrange
        when(userRepository.existsById("testUserId")).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart("testUserId", "testProductId"));

        assertEquals("User not found with ID: testUserId", exception.getMessage());
        verify(userRepository, times(1)).existsById("testUserId");
        verify(productRepository, never()).findById(any());
        verify(cartRepository, never()).findByUserId(any());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void testAddToCart_ProductNotFound() {
        // Arrange
        when(userRepository.existsById("testUserId")).thenReturn(true);
        when(productRepository.findById("testProductId")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart("testUserId", "testProductId"));

        assertEquals("Product not found with ID: testProductId", exception.getMessage());
        verify(userRepository, times(1)).existsById("testUserId");
        verify(productRepository, times(1)).findById("testProductId");
        verify(cartRepository, never()).findByUserId(any());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void testRemoveFromCart() {
        // Arrange - First add a product to the cart
        cart.addProduct(product);
        
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        // Act
        String result = cartService.removeFromCart("testUserId", "testProductId");

        // Assert
        assertEquals("Product removed from cart.", result);
        verify(cartRepository, times(1)).findByUserId("testUserId");
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveFromCart_CartNotFound() {
        // Arrange
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeFromCart("testUserId", "testProductId"));

        assertEquals("Cart not found for user ID: testUserId", exception.getMessage());
        verify(cartRepository, times(1)).findByUserId("testUserId");
        verify(cartRepository, never()).save(any());
    }

    @Test
    void testViewCart() {
        // Arrange
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.of(cart));

        // Act
        Cart result = cartService.viewCart("testUserId");

        // Assert
        assertNotNull(result);
        assertEquals("testUserId", result.getUserId());
        verify(cartRepository, times(1)).findByUserId("testUserId");
    }

    @Test
    void testViewCart_NotFound() {
        // Arrange
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.viewCart("testUserId"));

        assertEquals("Cart not found for user ID: testUserId", exception.getMessage());
        verify(cartRepository, times(1)).findByUserId("testUserId");
    }
}