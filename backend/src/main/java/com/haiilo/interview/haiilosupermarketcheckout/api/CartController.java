package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.CheckoutService;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.CartRepository;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        log.info("REST request to create a new shopping cart");
        Cart cart = cartRepository.save(new Cart());
        return ResponseEntity.status(201).body(cart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID cartId) {
        log.debug("REST request to get Cart : {}", cartId);
        return cartRepository.findById(cartId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cartId}/items")
    @Transactional
    public ResponseEntity<Void> addProductToCart(
            @PathVariable UUID cartId,
            @RequestParam UUID productId,
            @RequestParam(defaultValue = "1") int quantity) {

        log.info("REST request to add product {} (qty: {}) to cart {}", productId, quantity, cartId);

        if (quantity <= 0) {
            log.error("Validation failed: quantity must be positive");
            return ResponseEntity.badRequest().build();
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cart.addProduct(product, quantity);
        cartRepository.save(cart);

        log.info("Product successfully added to cart");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/total")
    public ResponseEntity<BigDecimal> getCartTotal(@PathVariable UUID cartId) {
        log.info("REST request to calculate total for cart {}", cartId);

        return cartRepository.findById(cartId)
                .map(cart -> {
                    BigDecimal total = checkoutService.calculateTotal(cart);
                    log.info("Calculation successful for cart {}: {}", cartId, total);
                    return ResponseEntity.ok(total);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}