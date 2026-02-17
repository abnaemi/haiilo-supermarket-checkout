package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.WeeklyOfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {

    @Mock
    private WeeklyOfferRepository offerRepository;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    private Product apple;
    private Product banana;

    @BeforeEach
    void setUp() {
        apple = new Product("Apple", new BigDecimal("0.30"));
        apple.setId(UUID.randomUUID());

        banana = new Product("Banana", new BigDecimal("0.50"));
        banana.setId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Should calculate standard price when no offers exist")
    void shouldCalculateStandardPrice() {
        Cart cart = new Cart();
        cart.addProduct(apple, 1);
        cart.addProduct(banana, 1);

        when(offerRepository.findFirstByProductId(apple.getId())).thenReturn(Optional.empty());
        when(offerRepository.findFirstByProductId(banana.getId())).thenReturn(Optional.empty());

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("0.80"), total);
    }

    @Test
    @DisplayName("Should apply bundle offer correctly (e.g., 3 apples with a '2 for 0.45' offer)")
    void shouldApplyBundleOffer() {
        Cart cart = new Cart();
        cart.addProduct(apple, 3); // 2 in bundle + 1 individual

        WeeklyOffer appleOffer = new WeeklyOffer(apple, 2, new BigDecimal("0.45"));
        when(offerRepository.findFirstByProductId(apple.getId())).thenReturn(Optional.of(appleOffer));

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("0.75"), total);
    }

    @Test
    @DisplayName("Should use standard price if minimum quantity for offer is not reached")
    void shouldNotApplyOfferIfQuantityTooLow() {
        Cart cart = new Cart();
        cart.addProduct(apple, 1);

        WeeklyOffer appleOffer = new WeeklyOffer(apple, 2, new BigDecimal("0.45"));
        when(offerRepository.findFirstByProductId(apple.getId())).thenReturn(Optional.of(appleOffer));

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("0.30"), total);
    }

    @Test
    @DisplayName("Should return 0.00 for an empty cart")
    void shouldReturnZeroForEmptyCart() {
        Cart cart = new Cart();
        BigDecimal total = checkoutService.calculateTotal(cart);
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    @DisplayName("Should calculate complex mixed cart with multiple products and offers")
    void shouldCalculateComplexMixedCart() {
        Cart cart = new Cart();
        cart.addProduct(apple, 3);
        cart.addProduct(banana, 2);

        WeeklyOffer appleOffer = new WeeklyOffer(apple, 2, new BigDecimal("0.45"));

        when(offerRepository.findFirstByProductId(apple.getId())).thenReturn(Optional.of(appleOffer));
        when(offerRepository.findFirstByProductId(banana.getId())).thenReturn(Optional.empty());

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("1.75"), total);
    }
}