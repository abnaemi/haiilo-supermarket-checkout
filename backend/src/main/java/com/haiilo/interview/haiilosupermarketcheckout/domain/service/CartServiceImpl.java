package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.CartItemRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Override
    public Cart createCart() {
        log.info("Creating a new shopping cart");
        return cartRepository.save(new Cart());
    }

    @Override
    public void addOrUpdateProduct(UUID cartId, CartItemRequestDTO request) {
        log.info("Adding product {} to cart {}", request.productId(), cartId);
        Cart cart = getCartById(cartId);
        Product product = productService.getProductEntityById(request.productId());

        cart.addProduct(product, request.quantity());
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(UUID cartId, UUID productId, int newQuantity) {
        log.info("Updating product {} in cart {} to quantity {}", productId, cartId, newQuantity);
        Cart cart = getCartById(cartId);

        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(newQuantity),
                        () -> { throw new EntityNotFoundException("Product not found in cart"); } // Fix: Specific exception
                );

        cartRepository.save(cart);
    }

    @Override
    public void deleteCart(UUID cartId) {
        log.info("Deleting cart {}", cartId);
        if (!cartRepository.existsById(cartId)) {
            throw new EntityNotFoundException("Cart not found: " + cartId);
        }
        cartRepository.deleteById(cartId);
    }

    @Override
    public Cart getCartById(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found: " + cartId));
    }
}