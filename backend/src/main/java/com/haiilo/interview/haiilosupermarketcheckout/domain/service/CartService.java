package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.CartItemRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import java.util.UUID;

public interface CartService {
    Cart createCart();
    Cart getCartById(UUID cartId);
    void addOrUpdateProduct(UUID cartId, CartItemRequestDTO request);
    void updateItemQuantity(UUID cartId, UUID productId, int newQuantity);
    void deleteCart(UUID cartId);
}