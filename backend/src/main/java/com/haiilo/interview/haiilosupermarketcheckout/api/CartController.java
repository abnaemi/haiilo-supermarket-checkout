package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.CartItemRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.CartService;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService cartService;
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID cartId) {
        return ResponseEntity.ok(cartService.getCartById(cartId));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Void> addProduct(@PathVariable UUID cartId, @RequestBody @Valid CartItemRequestDTO request) {
        cartService.addOrUpdateProduct(cartId, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> updateQuantity(@PathVariable UUID cartId, @PathVariable UUID productId, @RequestParam int quantity) {
        cartService.updateItemQuantity(cartId, productId, quantity);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/total")
    public ResponseEntity<BigDecimal> getTotal(@PathVariable UUID cartId) {
        Cart cart = cartService.getCartById(cartId);
        return ResponseEntity.ok(checkoutService.calculateTotal(cart));
    }
}