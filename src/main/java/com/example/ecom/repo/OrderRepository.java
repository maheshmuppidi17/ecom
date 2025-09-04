package com.example.ecom.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ecom.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserIdAndDateTimeAfter(String userId, LocalDateTime dateTime);
}
