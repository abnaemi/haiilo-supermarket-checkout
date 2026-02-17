package com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest // Lädt nur JPA-Komponenten und nutzt In-Memory DB
class WeeklyOfferRepositoryTest {

    @Autowired
    private WeeklyOfferRepository weeklyOfferRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should find a weekly offer by its associated product ID")
    void shouldFindFirstByProductId() {
        // Arrange
        Product apple = new Product("Apple", new BigDecimal("0.30"));
        Product savedProduct = productRepository.save(apple);

        WeeklyOffer offer = new WeeklyOffer(savedProduct, 2, new BigDecimal("0.45"));
        weeklyOfferRepository.save(offer);

        // Act
        Optional<WeeklyOffer> foundOffer = weeklyOfferRepository.findFirstByProductId(savedProduct.getId());

        // Assert
        assertTrue(foundOffer.isPresent(), "Offer should be found");
        assertEquals(2, foundOffer.get().getRequiredQuantity());
        assertEquals(0, new BigDecimal("0.45").compareTo(foundOffer.get().getOfferPrice()));
    }
}