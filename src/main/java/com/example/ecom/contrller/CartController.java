package com.example.ecom.contrller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecom.model.Cart;
import com.example.ecom.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam String  userId, @RequestParam String  productId) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam String  userId, @RequestParam String  productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    @GetMapping("/view")
    public ResponseEntity<Cart> viewCart(@RequestParam String  userId) {
        return ResponseEntity.ok(cartService.viewCart(userId));
    }
}
