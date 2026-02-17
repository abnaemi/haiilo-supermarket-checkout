package com.haiilo.interview.haiilosupermarketcheckout.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record OfferRequestDTO(
        @NotNull UUID productId,
        @Min(2) int requiredQuantity,
        @NotNull @Min(0) BigDecimal offerPrice
) {}