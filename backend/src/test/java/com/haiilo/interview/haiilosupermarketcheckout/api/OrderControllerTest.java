package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.CheckoutItemRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.CustomerDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OrderRequestDTO;
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
import java.util.Collections;
import java.util.List;
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
    @DisplayName("POST /api/v1/orders/checkout should return 201 Created and the calculated order")
    void shouldCreateOrderFromCheckoutItems() throws Exception {
        CheckoutItemRequestDTO checkoutItem = new CheckoutItemRequestDTO();
        checkoutItem.setProductId(UUID.randomUUID());
        checkoutItem.setQuantity(2);
        List<CheckoutItemRequestDTO> items = Collections.singletonList(checkoutItem);

        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setStreet("123 Main St");
        customer.setCity("New York");
        customer.setCountry("USA");
        customer.setPhoneNumber("+1 555 1234");
        customer.setEmail("john.doe@example.com");

        OrderRequestDTO requestBody = new OrderRequestDTO();
        requestBody.setItems(items);
        requestBody.setCustomer(customer);

        Order calculatedOrder = new Order();
        calculatedOrder.setId(UUID.randomUUID());
        calculatedOrder.setFinalTotalPrice(new BigDecimal("1.80"));
        calculatedOrder.setTotalOriginalPrice(new BigDecimal("2.00"));
        calculatedOrder.setTotalDiscountAmount(new BigDecimal("0.20"));

        when(orderService.placeOrder(any(OrderRequestDTO.class))).thenReturn(calculatedOrder);

        mockMvc.perform(post("/api/v1/orders/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.finalTotalPrice").value(1.80))
                .andExpect(jsonPath("$.totalOriginalPrice").value(2.00))
                .andExpect(jsonPath("$.totalDiscountAmount").value(0.20));
    }
}
