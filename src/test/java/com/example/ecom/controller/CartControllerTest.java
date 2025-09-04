package com.example.ecom.controller;

import com.example.ecom.contrller.CartController;
import com.example.ecom.model.Cart;
import com.example.ecom.model.CartItem;
import com.example.ecom.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart() {
        String userId = "testUser";
        String productId = "prod1";
        int quantity = 2;
        String expectedResponse = "Added 2 x Test Product to cart.";

        when(cartService.addToCart(userId, productId, quantity)).thenReturn(expectedResponse);

        ResponseEntity<String> response = cartController.addToCart(userId, productId, quantity);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(cartService, times(1)).addToCart(userId, productId, quantity);
    }

    @Test
    void testRemoveFromCart() {
        String userId = "testUser";
        String productId = "prod1";
        String expectedResponse = "Product removed from cart.";

        when(cartService.removeFromCart(userId, productId)).thenReturn(expectedResponse);

        ResponseEntity<String> response = cartController.removeFromCart(userId, productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(cartService, times(1)).removeFromCart(userId, productId);
    }

    @Test
    void testViewCart() {
        String userId = "testUser";
        Cart cart = new Cart();
        cart.setId("cart1");
        cart.setUserId(userId);
        cart.setItems(Collections.singletonList(
                new CartItem("prod1", "Test Product", 100.0, 2)
        ));

        when(cartService.viewCart(userId)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.viewCart(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cart, response.getBody());
        verify(cartService, times(1)).viewCart(userId);
    }

    @Test
    void testClearCart() {
        String userId = "testUser";
        String expectedResponse = "Cart cleared.";

        when(cartService.clearCart(userId)).thenReturn(expectedResponse);

        ResponseEntity<String> response = cartController.clearCart(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(cartService, times(1)).clearCart(userId);
    }
}
