package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.CartItem;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.WeeklyOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final WeeklyOfferRepository offerRepository;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotal(Cart cart) {
        log.info("Starting total price calculation for cart: {}", cart.getId());

        return cart.getItems().stream()
                .map(this::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateItemTotal(CartItem item) {
        var product = item.getProduct();
        int quantity = item.getQuantity();

        Optional<WeeklyOffer> offerOpt = offerRepository.findFirstByProductId(product.getId());

        if (offerOpt.isPresent()) {
            WeeklyOffer offer = offerOpt.get();
            return calculateBundlePrice(quantity, product.getPrice(), offer);
        }

        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal calculateBundlePrice(int quantity, BigDecimal standardPrice, WeeklyOffer offer) {
        int requiredQty = offer.getRequiredQuantity();
        BigDecimal bundlePrice = offer.getOfferPrice();

        int numBundles = quantity / requiredQty;
        int remainder = quantity % requiredQty;

        BigDecimal totalBundleCost = bundlePrice.multiply(BigDecimal.valueOf(numBundles));
        BigDecimal totalRemainderCost = standardPrice.multiply(BigDecimal.valueOf(remainder));

        BigDecimal total = totalBundleCost.add(totalRemainderCost);

        log.debug("Applied offer for product {}: {} bundles and {} remainder. Subtotal: {}",
                offer.getProduct().getName(), numBundles, remainder, total);

        return total;
    }
}