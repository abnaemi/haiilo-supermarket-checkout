package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.CheckoutItemRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(List<CheckoutItemRequestDTO> checkoutItems);
}
