package com.example.ecom.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecom.dto.FundTransferDTO;
import com.example.ecom.exception.CartEmptyException;
import com.example.ecom.exception.CartNotFoundException;
import com.example.ecom.exception.PaymentFailedException;

import com.example.ecom.kafka.KafkaProducerService;
import com.example.ecom.model.Cart;
import com.example.ecom.model.Order;
import com.example.ecom.repo.CartRepository;
import com.example.ecom.repo.OrderRepository;
import com.example.ecom.service.BankingClient;
import com.example.ecom.service.OrderService;
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired 
    private CartRepository cartRepository;

    @Autowired 
    private OrderRepository orderRepository;

    @Autowired 
    private BankingClient bankingClient;

    @Autowired
    private KafkaProducerService kafkaProducerService; // <-- Kafka producer to send order events

    @Override
    public String checkout(String userId, String accountNumber) {
        // 1. Get user cart
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException("Cart not found for user " + userId));

        if (cart.getProducts() == null || cart.getProducts().isEmpty()) {
            throw new CartEmptyException("Cart is empty for user " + userId);
        }

        double total = cart.calculateTotal();

        // 2. Prepare transfer request
        FundTransferDTO transferDTO = new FundTransferDTO();
        transferDTO.setFromAccount(accountNumber);
        transferDTO.setToAccount("ECOM123456"); // Merchant account
        transferDTO.setAmount(total);

        // 3. Call Banking service
        String response = bankingClient.transact(transferDTO);
        if (!response.toLowerCase().contains("successful")) {
            throw new PaymentFailedException("Payment failed: " + response);
        }

        // 4. Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setDateTime(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setProducts(new ArrayList<>(cart.getProducts()));

        orderRepository.save(order);

        // 5. Publish order event to Kafka for DeliveryService
        kafkaProducerService.sendOrderEvent(order);

        // 6. Clear cart
        cart.clear();
        cartRepository.save(cart);

        return "Order placed successfully! Order ID: " + order.getId() + 
                "\nTotal Amount: ₹" + String.format("%.2f", total) +
                "\n" + "Delivery processing taking longer than expected. You will receive updates shortly.";
    }

    @Override
    public List<Order> getUserOrders(String userId, int months) {
        LocalDateTime fromDate = LocalDateTime.now().minusMonths(months);
        return orderRepository.findByUserIdAndDateTimeAfter(userId, fromDate);
    }
}


