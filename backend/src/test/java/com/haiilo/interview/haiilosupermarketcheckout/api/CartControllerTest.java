package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.CartItemRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.CartService;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.CheckoutService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private CheckoutService checkoutService;

    private UUID cartId;

    @BeforeEach
    void setUp() {
        cartId = UUID.randomUUID();
    }

    @Test
    void createCart_ShouldReturnCreated() throws Exception {
        Cart newCart = new Cart();
        newCart.setId(cartId);

        when(cartService.createCart()).thenReturn(newCart);

        mockMvc.perform(post("/api/v1/carts"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cartId.toString()));
    }

    @Test
    void addProduct_ShouldReturnNoContent() throws Exception {
        CartItemRequestDTO request = new CartItemRequestDTO(UUID.randomUUID(), 5);

        doNothing().when(cartService).addOrUpdateProduct(eq(cartId), any(CartItemRequestDTO.class));

        mockMvc.perform(post("/api/v1/carts/{cartId}/items", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(cartService).addOrUpdateProduct(eq(cartId), any(CartItemRequestDTO.class));
    }

    @Test
    void updateQuantity_ShouldReturnNoContent() throws Exception {
        UUID productId = UUID.randomUUID();
        int newQuantity = 10;

        doNothing().when(cartService).updateItemQuantity(cartId, productId, newQuantity);

        mockMvc.perform(put("/api/v1/carts/{cartId}/items/{productId}", cartId, productId)
                        .param("quantity", String.valueOf(newQuantity)))
                .andExpect(status().isNoContent());

        verify(cartService).updateItemQuantity(cartId, productId, newQuantity);
    }

    @Test
    void getTotal_ShouldReturnPrice() throws Exception {
        Cart cart = new Cart();
        BigDecimal expectedTotal = new BigDecimal("12.50");

        when(cartService.getCartById(cartId)).thenReturn(cart);
        when(checkoutService.calculateTotal(cart)).thenReturn(expectedTotal);

        mockMvc.perform(get("/api/v1/carts/{cartId}/total", cartId))
                .andExpect(status().isOk())
                .andExpect(content().string("12.50"));
    }

    @Test
    void getCart_ShouldReturnNotFound_WhenCartDoesNotExist() throws Exception {
        when(cartService.getCartById(cartId)).thenThrow(new EntityNotFoundException("Cart not found"));

        mockMvc.perform(get("/api/v1/carts/{cartId}", cartId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cart not found"));
    }
    @Test
    void getCart_ShouldReturn404_WhenCartNotFound() throws Exception {

        when(cartService.getCartById(cartId)).thenThrow(new EntityNotFoundException("Cart not found"));

        mockMvc.perform(get("/api/v1/carts/{cartId}", cartId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Cart not found"));
    }
}