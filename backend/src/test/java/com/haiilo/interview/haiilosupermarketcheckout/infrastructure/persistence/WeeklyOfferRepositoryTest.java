package com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5433/supermarket",
        "spring.datasource.username=user",
        "spring.datasource.password=secret",
        "spring.datasource.driver-class-name=org.postgresql.Driver",
        "spring.docker.compose.enabled=false"
})
class WeeklyOfferRepositoryTest {

    @Autowired
    private WeeklyOfferRepository weeklyOfferRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should find a weekly offer by its associated product ID")
    void shouldFindFirstByProductId() {
        Product apple = new Product("Apple", new BigDecimal("0.30"));
        Product savedProduct = productRepository.save(apple);

        WeeklyOffer offer = new WeeklyOffer(savedProduct, 2, new BigDecimal("0.45"));
        weeklyOfferRepository.save(offer);

        Optional<WeeklyOffer> foundOffer = weeklyOfferRepository.findFirstByProductId(savedProduct.getId());

        assertTrue(foundOffer.isPresent(), "Offer should be found");
        assertEquals(2, foundOffer.get().getRequiredQuantity());
        assertEquals(new BigDecimal("0.45"), foundOffer.get().getOfferPrice());
    }
}