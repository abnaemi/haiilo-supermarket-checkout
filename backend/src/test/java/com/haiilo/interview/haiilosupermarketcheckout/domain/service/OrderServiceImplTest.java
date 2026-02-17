package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Order;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.OrderItem;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Should save order and return the saved object with an ID")
    void shouldSaveOrderSuccessfully() {
        Order order = new Order();
        order.setTotalAmount(new BigDecimal("15.50"));

        OrderItem item = new OrderItem();
        item.setProductName("Apple");
        item.setQuantity(5);
        item.setPriceAtPurchase(new BigDecimal("0.30"));
        order.setItems(List.of(item));

        Order savedOrder = new Order();
        savedOrder.setId(UUID.randomUUID());
        savedOrder.setTotalAmount(order.getTotalAmount());
        savedOrder.setItems(order.getItems());

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.saveOrder(order);

        assertNotNull(result.getId(), "Saved order should have an ID");
        assertEquals(new BigDecimal("15.50"), result.getTotalAmount());
        assertEquals(1, result.getItems().size());

        verify(orderRepository, times(1)).save(order);
    }
}