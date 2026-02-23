package com.haiilo.interview.haiilosupermarketcheckout.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    @Valid
    @NotNull
    private CustomerDTO customer;

    @Valid
    @NotEmpty
    private List<CheckoutItemRequestDTO> items;
}
