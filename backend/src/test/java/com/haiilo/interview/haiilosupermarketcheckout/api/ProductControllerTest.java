package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductResponseDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        ProductResponseDTO product = new ProductResponseDTO(UUID.randomUUID(), "Apple", BigDecimal.valueOf(0.30));
        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[0].price").value(0.30));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void createProduct_ShouldReturnCreated() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO("Banana", BigDecimal.valueOf(0.50));
        ProductResponseDTO response = new ProductResponseDTO(UUID.randomUUID(), "Banana", BigDecimal.valueOf(0.50));

        when(productService.createProduct(any(ProductRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Banana"))
                .andExpect(jsonPath("$.price").value(0.50));

        verify(productService, times(1)).createProduct(any(ProductRequestDTO.class));
    }

    @Test
    void createProduct_ShouldReturnBadRequest_WhenPriceIsNegative() throws Exception {
        ProductRequestDTO invalidRequest = new ProductRequestDTO("Invalid", BigDecimal.valueOf(-1.00));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void createProduct_ShouldReturn400_WhenPriceIsInvalid() throws Exception {
        ProductRequestDTO invalidRequest = new ProductRequestDTO("Apple", BigDecimal.valueOf(-1.00));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(containsString("price")));
    }
}