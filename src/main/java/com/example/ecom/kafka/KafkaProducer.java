package com.example.ecom.kafka;

import com.example.ecom.dto.OrderEvent;
import com.example.ecom.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer implements KafkaProducerService {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendOrderEvent(Order order) {
        // Convert Order to OrderEvent DTO
        OrderEvent orderEvent = new OrderEvent(order);
        kafkaTemplate.send("order-events", orderEvent);
        System.out.println("Order event sent to Kafka: " + orderEvent.getId());
    }
}