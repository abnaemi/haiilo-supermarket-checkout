package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OrderRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Order;

public interface OrderService {
    Order placeOrder(OrderRequestDTO orderRequest);
}
