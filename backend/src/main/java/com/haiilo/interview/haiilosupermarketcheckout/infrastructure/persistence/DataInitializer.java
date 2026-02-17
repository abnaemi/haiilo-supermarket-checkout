package com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final WeeklyOfferRepository offerRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            log.info("Database empty, initializing products and offers...");

            Product apple = new Product("Apple", BigDecimal.valueOf(0.90));
            Product banana = new Product("Banana", BigDecimal.valueOf(0.90));
            Product bread = new Product("Bread", BigDecimal.valueOf(0.90));
            Product milk = new Product("Milk", BigDecimal.valueOf(0.90));

            productRepository.saveAll(List.of(apple, banana, bread, milk));

            List<WeeklyOffer> offers = List.of(
                    new WeeklyOffer(apple, 2, BigDecimal.valueOf(1.00)),
                    new WeeklyOffer(banana, 3, BigDecimal.valueOf(2.00)),
                    new WeeklyOffer(bread, 2, BigDecimal.valueOf(1.60)),
                    new WeeklyOffer(milk, 5, BigDecimal.valueOf(2.00))
            );

            offerRepository.saveAll(offers);
            log.info("Successfully initialized 4 products and 4 weekly offers.");
        }
    }
}