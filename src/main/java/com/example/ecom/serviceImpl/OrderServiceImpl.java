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
import com.example.ecom.model.CartItem;
import com.example.ecom.model.Offer;
import com.example.ecom.model.Order;
import com.example.ecom.repo.CartRepository;
import com.example.ecom.repo.OrderRepository;
import com.example.ecom.service.BankingClient;
import com.example.ecom.service.OfferService;
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
    private KafkaProducerService kafkaProducerService;
    
    @Autowired
    private OfferService offerService;

    @Override
    public String checkout(String userId, String accountNumber,String offerCode) {
        
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user " + userId));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new CartEmptyException("Cart is empty for user " + userId);
        }

        double total = cart.calculateTotal();
        if (offerCode != null && !offerCode.isEmpty()) {
            Offer offer = offerService.getOfferByCode(offerCode);
            double discount = (total * offer.getDiscountPercentage()) / 100.0;
            total -= discount;
        }
        
       
        
        FundTransferDTO transferDTO = new FundTransferDTO();
        transferDTO.setFromAccount(accountNumber);
        transferDTO.setToAccount("ECOM123456"); // Merchant account
        transferDTO.setAmount(total);

       
        String response = bankingClient.transact(transferDTO);
        if (!response.toLowerCase().contains("successful")) {
            throw new PaymentFailedException("Payment failed: " + response);
        }

        
        Order order = new Order();
        order.setUserId(userId);
        order.setDateTime(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setItems(new ArrayList<>(cart.getItems())); // save CartItem snapshot
        
       
        orderRepository.save(order);

        
        kafkaProducerService.sendOrderEvent(order);

       
        cart.clear();
        cartRepository.save(cart);

        return "Order placed successfully! Order ID: " + order.getId() +
                "\nTotal Amount: ₹" + String.format("%.2f", total) +
                "\nDelivery is being processed. You will receive updates shortly.";
    }

    @Override
    public List<Order> getUserOrders(String userId, int months) {
        LocalDateTime fromDate = LocalDateTime.now().minusMonths(months);
        return orderRepository.findByUserIdAndDateTimeAfter(userId, fromDate);
    }
}
