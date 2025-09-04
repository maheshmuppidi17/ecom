package com.example.ecom.kafka;

import com.example.ecom.model.Order;

public interface KafkaProducerService {
    void sendOrderEvent(Order order);
}