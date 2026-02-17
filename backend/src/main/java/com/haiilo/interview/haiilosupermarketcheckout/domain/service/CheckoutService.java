package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import java.math.BigDecimal;

public interface CheckoutService {

    BigDecimal calculateTotal(Cart cart);
}