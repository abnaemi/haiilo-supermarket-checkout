package com.haiilo.interview.haiilosupermarketcheckout.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WeeklyOfferDTO(
        UUID id,
        UUID productId,
        String productName,
        Integer requiredQuantity,
        BigDecimal offerPrice
) {}