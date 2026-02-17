package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Order;

public interface OrderService {
    Order saveOrder(Order order);
}