package com.example.ecom.contrller;
import com.example.ecom.model.Cart;
import com.example.ecom.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart Management", description = "APIs for managing user's shopping cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "Add product to cart", description = "Adds a product with a specific quantity to the user's cart and updates stock.")
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @Operation(summary = "Remove product from cart", description = "Removes a product from the user's cart and restores stock.")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(
            @RequestParam String userId,
            @RequestParam String productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    @Operation(summary = "View user's cart", description = "Fetches all items in the user's cart.")
    @GetMapping("/view")
    public ResponseEntity<Cart> viewCart(@RequestParam String userId) {
        return ResponseEntity.ok(cartService.viewCart(userId));
    }

    @Operation(summary = "Clear cart", description = "Clears the user's cart and restores all product stock.")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam String userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}
