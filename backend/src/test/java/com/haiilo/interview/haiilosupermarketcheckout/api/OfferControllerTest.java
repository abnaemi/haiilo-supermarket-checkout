package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OfferRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.WeeklyOfferDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.OfferService;
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

@WebMvcTest(OfferController.class)
class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OfferService offerService;

    private UUID productId;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        testProduct = new Product("Apple", BigDecimal.valueOf(0.30));
        testProduct.setId(productId);
    }

    @Test
    void createOffer_ShouldReturnCreated() throws Exception {
        OfferRequestDTO request = new OfferRequestDTO(productId, 2, BigDecimal.valueOf(0.45));
        WeeklyOfferDTO savedOfferDto = new WeeklyOfferDTO(
                UUID.randomUUID(),
                productId,
                "Apple",
                2,
                BigDecimal.valueOf(0.45)
        );
        when(offerService.createOrUpdateOffer(any(OfferRequestDTO.class))).thenReturn(savedOfferDto);
        mockMvc.perform(post("/api/v1/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requiredQuantity").value(2))
                .andExpect(jsonPath("$.offerPrice").value(0.45));

        verify(offerService, times(1)).createOrUpdateOffer(any(OfferRequestDTO.class));
    }

    @Test
    void createOffer_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        //noinspection ConstantConditions
        OfferRequestDTO invalidRequest = new OfferRequestDTO(productId, 1, BigDecimal.valueOf(0.45));

        mockMvc.perform(post("/api/v1/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(offerService);
    }

    @Test
    void deleteOffer_ShouldReturnNoContent() throws Exception {
        UUID offerId = UUID.randomUUID();
        doNothing().when(offerService).deleteOffer(offerId);

        mockMvc.perform(delete("/api/v1/offers/{id}", offerId))
                .andExpect(status().isNoContent());

        verify(offerService, times(1)).deleteOffer(offerId);
    }

    @Test
    void deleteOffer_ShouldReturnNotFound_WhenOfferDoesNotExist() throws Exception {
        UUID offerId = UUID.randomUUID();
        doThrow(new EntityNotFoundException("Offer not found")).when(offerService).deleteOffer(offerId);

        mockMvc.perform(delete("/api/v1/offers/{id}", offerId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(offerService).deleteOffer(offerId);
    }
}