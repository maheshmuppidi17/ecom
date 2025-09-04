package com.example.ecom.serviceImpl;

import com.example.ecom.dto.FundTransferDTO;
import com.example.ecom.kafka.KafkaProducer; // Add this import
import com.example.ecom.kafka.KafkaProducerService;
import com.example.ecom.model.Cart;
import com.example.ecom.model.Order;
import com.example.ecom.model.Product;
import com.example.ecom.repo.CartRepository;
import com.example.ecom.repo.OrderRepository;
import com.example.ecom.service.BankingClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class) // Use this instead of MockitoAnnotations
public class OrderServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BankingClient bankingClient;

    @Mock // Add this mock for KafkaProducer
    private KafkaProducerService kafkaProducer;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        // Remove MockitoAnnotations.openMocks(this) when using @ExtendWith

        product = new Product();
        product.setId("testProductId");
        product.setName("Laptop");
        product.setPrice(1000.0);

        cart = new Cart();
        cart.setId("setCartId");
        cart.setUserId("testUserId");
        cart.addProduct(product);
    }

    @Test
    void testCheckout_Success() {
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.of(cart));
        when(bankingClient.transact(any(FundTransferDTO.class)))
                .thenReturn("Transaction successful");
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId("testOrderId");
            return order;
        });

        String response = orderService.checkout("testUserId", "ACC123");

        assertTrue(response.contains("Order placed successfully"));
        verify(cartRepository).findByUserId("testUserId");
        verify(bankingClient).transact(any(FundTransferDTO.class));
        verify(orderRepository).save(any(Order.class));
        verify(cartRepository).save(cart);
        verify(kafkaProducer).sendOrderEvent(any(Order.class)); // Add this verification
    }

    @Test
    void testCheckout_CartNotFound() {
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.checkout("testUserId", "ACC123"));
        assertEquals("Cart not found for user testUserId", ex.getMessage());
        
        verify(kafkaProducer, never()).sendOrderEvent(any(Order.class)); // Add this
    }

    @Test
    void testCheckout_EmptyCart() {
        cart.clear();
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.of(cart));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.checkout("testUserId", "ACC123"));
        assertEquals("Cart is empty for user testUserId", ex.getMessage());
        
        verify(kafkaProducer, never()).sendOrderEvent(any(Order.class)); // Add this
    }

    @Test
    void testCheckout_TransactionFailed() {
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.of(cart));
        when(bankingClient.transact(any(FundTransferDTO.class)))
                .thenReturn("Transaction failed: Insufficient balance");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.checkout("testUserId", "ACC123"));
        assertTrue(ex.getMessage().contains("Payment failed"));
        verify(orderRepository, never()).save(any(Order.class));
        verify(kafkaProducer, never()).sendOrderEvent(any(Order.class)); // Add this
    }

    @Test
    void testGetUserOrders() {
        Order order1 = new Order();
        order1.setId("order1");
        order1.setUserId("testUserId");
        Order order2 = new Order();
        order2.setId("order2");
        order2.setUserId("testUserId");

        when(orderRepository.findByUserIdAndDateTimeAfter(eq("testUserId"), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getUserOrders("testUserId", 3);

        assertEquals(2, orders.size());
        verify(orderRepository).findByUserIdAndDateTimeAfter(eq("testUserId"), any(LocalDateTime.class));
    }
}