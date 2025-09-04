package com.example.ecom.controller;

import com.example.ecom.contrller.CartController;
import com.example.ecom.model.Cart;
import com.example.ecom.model.Product;
import com.example.ecom.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testAddToCart() throws Exception {
        String userId = "testUserId";
        String productId = "testProductId";
        String responseMessage = "Item added successfully";

        when(cartService.addToCart(userId, productId)).thenReturn(responseMessage);

        mockMvc.perform(post("/api/cart/add")
                        .param("userId", userId)
                        .param("productId", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }

    @Test
    void testRemoveFromCart() throws Exception {
        String userId = "testUserId";
        String productId = "testProductId";
        String responseMessage = "Item removed successfully";

        when(cartService.removeFromCart(userId, productId)).thenReturn(responseMessage);

        mockMvc.perform(delete("/api/cart/remove")
                        .param("userId", userId)
                        .param("productId", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }

    @Test
    void testViewCart() throws Exception {
        String userId = "testUserId";

        // Prepare cart with String ID
        Cart cart = new Cart();
        cart.setId(userId);
        cart.setUserId(userId);

        // Add a sample product
        Product product = new Product();
        product.setId("prod1");
        product.setName("Test Product");
        product.setPrice(100);
        product.setQuantity(1);

        cart.setProducts(new ArrayList<>());
        cart.getProducts().add(product);

        when(cartService.viewCart(userId)).thenReturn(cart);

        mockMvc.perform(get("/api/cart/view")
                        .param("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.products[0].id").value("prod1"))
                .andExpect(jsonPath("$.products[0].name").value("Test Product"))
                .andExpect(jsonPath("$.products[0].price").value(100.0))
                .andExpect(jsonPath("$.products[0].quantity").value(1));
    }
}
