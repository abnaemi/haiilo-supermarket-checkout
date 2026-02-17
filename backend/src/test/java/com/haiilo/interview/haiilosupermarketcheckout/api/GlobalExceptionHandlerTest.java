package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.haiilo.interview.haiilosupermarketcheckout.domain.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void handleEntityNotFound_ShouldReturn404() throws Exception {
        when(productService.getAllProducts()).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    @Test
    void handleRuntimeException_ShouldNowReturn500() throws Exception {

        when(productService.getAllProducts()).thenThrow(new RuntimeException("Technical glitch"));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    @Test
    void handleNullPointerException_ShouldReturn500() throws Exception {
        when(productService.getAllProducts()).thenThrow(new NullPointerException("NPE"));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}