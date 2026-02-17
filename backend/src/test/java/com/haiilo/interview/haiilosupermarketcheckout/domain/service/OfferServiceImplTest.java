package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OfferRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.WeeklyOfferDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.WeeklyOfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private WeeklyOfferRepository offerRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
    void createOrUpdateOffer_ShouldReplaceExistingOffer() {
        UUID productId = UUID.randomUUID();
        Product product = new Product("Apple", BigDecimal.valueOf(0.30));
        product.setId(productId);

        OfferRequestDTO request = new OfferRequestDTO(productId, 3, BigDecimal.valueOf(0.70));
        WeeklyOffer oldOffer = new WeeklyOffer(product, 2, BigDecimal.valueOf(0.50));

        when(productService.getProductEntityById(productId)).thenReturn(product);
        when(offerRepository.findFirstByProductId(productId)).thenReturn(Optional.of(oldOffer));
        when(offerRepository.save(any(WeeklyOffer.class))).thenAnswer(i -> i.getArguments()[0]);

        WeeklyOfferDTO result = offerService.createOrUpdateOffer(request);

        verify(offerRepository).delete(oldOffer);
        verify(offerRepository).save(any(WeeklyOffer.class));

        assertThat(result.productId()).isEqualTo(productId);
        assertThat(result.requiredQuantity()).isEqualTo(3);
        assertThat(result.offerPrice()).isEqualByComparingTo(BigDecimal.valueOf(0.70));    }

    @Test
    void deleteOffer_success() {
        UUID offerId = UUID.randomUUID();
        when(offerRepository.existsById(offerId)).thenReturn(true);

        offerService.deleteOffer(offerId);

        verify(offerRepository).deleteById(offerId);
    }
}