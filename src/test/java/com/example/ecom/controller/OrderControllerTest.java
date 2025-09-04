package com.example.ecom.controller;

import com.example.ecom.contrller.OrderController;
import com.example.ecom.model.Order;
import com.example.ecom.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckout() {
        String userId = "testUserId";
        String accountNumber = "ACC12345";
        String expectedResponse = "Order placed successfully. Order ID: order123";

        when(orderService.checkout(userId, accountNumber)).thenReturn(expectedResponse);

        ResponseEntity<String> response = orderController.checkout(userId, accountNumber);

        assertEquals(expectedResponse, response.getBody());
        verify(orderService, times(1)).checkout(userId, accountNumber);
    }

    @Test
    void testGetDashboard() {
        String userId = "testUserId";
        int months = 3;

        // Prepare sample orders
        Order order1 = new Order();
        order1.setId("order1");
        order1.setUserId(userId);

        Order order2 = new Order();
        order2.setId("order2");
        order2.setUserId(userId);

        List<Order> mockOrders = Arrays.asList(order1, order2);

        when(orderService.getUserOrders(userId, months)).thenReturn(mockOrders);

        ResponseEntity<List<Order>> response = orderController.getDashboard(userId, months);

        assertEquals(2, response.getBody().size());
        assertEquals(mockOrders, response.getBody());
        verify(orderService, times(1)).getUserOrders(userId, months);
    }
}
