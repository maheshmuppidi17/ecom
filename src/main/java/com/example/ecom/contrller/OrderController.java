package com.example.ecom.contrller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecom.model.Order;
import com.example.ecom.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam String  userId, @RequestParam String accountNumber) {
        return ResponseEntity.ok(orderService.checkout(userId, accountNumber));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<Order>> getDashboard(@RequestParam String  userId, @RequestParam int months) {
        return ResponseEntity.ok(orderService.getUserOrders(userId, months));
    }
}
