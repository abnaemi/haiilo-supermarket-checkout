package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.CartItem;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.WeeklyOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final WeeklyOfferRepository offerRepository;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotal(Cart cart) {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return cart.getItems().stream()
                .map(this::calculateItemPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateItemPrice(CartItem item) {
        BigDecimal unitPrice = item.getProduct().getPrice();
        int quantity = item.getQuantity();

        Optional<WeeklyOffer> offerOpt = offerRepository.findFirstByProductId(item.getProduct().getId());

        if (offerOpt.isPresent()) {
            WeeklyOffer offer = offerOpt.get();

            if (quantity >= offer.getRequiredQuantity()) {
                int numBundles = quantity / offer.getRequiredQuantity();
                int remainder = quantity % offer.getRequiredQuantity();

                BigDecimal bundleTotal = offer.getOfferPrice().multiply(BigDecimal.valueOf(numBundles));
                BigDecimal remainderTotal = unitPrice.multiply(BigDecimal.valueOf(remainder));

                return bundleTotal.add(remainderTotal);
            }
        }

        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}