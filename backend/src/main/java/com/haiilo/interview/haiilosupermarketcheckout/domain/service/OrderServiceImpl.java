package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Order;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order saveOrder(Order order) {
        log.info("Processing new order with total amount: {}", order.getTotalAmount());

        Order savedOrder = orderRepository.save(order);

        log.info("Order successfully saved with ID: {}", savedOrder.getId());
        return savedOrder;
    }
}