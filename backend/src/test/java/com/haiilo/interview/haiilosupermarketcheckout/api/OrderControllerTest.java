package com.haiilo.interview.haiilosupermarketcheckout.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Order;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    @DisplayName("POST /api/v1/orders should return 201 Created and the saved order")
    void shouldCreateOrder() throws Exception {
        // GIVEN
        Order inputOrder = new Order();
        inputOrder.setTotalAmount(new BigDecimal("10.00"));

        Order savedOrder = new Order();
        savedOrder.setId(UUID.randomUUID());
        savedOrder.setTotalAmount(new BigDecimal("10.00"));

        when(orderService.saveOrder(any(Order.class))).thenReturn(savedOrder);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputOrder)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalAmount").value(10.00));
    }
}