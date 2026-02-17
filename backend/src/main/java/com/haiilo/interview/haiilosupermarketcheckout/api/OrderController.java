package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Order;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order placeOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }
}