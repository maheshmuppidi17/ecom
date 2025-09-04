package com.example.ecom.service;

import java.util.List;

import com.example.ecom.model.Order;

public interface OrderService {
    String checkout(String  userId, String accountNumber, String offerCode);
    List<Order> getUserOrders(String  userId, int months);
	
}
