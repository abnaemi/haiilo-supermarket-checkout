package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.CheckoutItemRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Order;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public Order placeOrder(@RequestBody @Valid List<CheckoutItemRequestDTO> checkoutItems) {
        return orderService.placeOrder(checkoutItems);
    }
}
