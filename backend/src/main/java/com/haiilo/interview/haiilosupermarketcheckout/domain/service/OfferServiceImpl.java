package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OfferRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.WeeklyOfferRepository;
import jakarta.persistence.EntityNotFoundException; // WICHTIGER IMPORT
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OfferServiceImpl implements OfferService {

    private final WeeklyOfferRepository offerRepository;
    private final ProductService productService;

    @Override
    public WeeklyOffer createOrUpdateOffer(OfferRequestDTO request) {
        log.info("Processing offer request for product ID: {}", request.productId());

        Product product = productService.getProductEntityById(request.productId());

        offerRepository.findFirstByProductId(product.getId())
                .ifPresent(existingOffer -> {
                    log.debug("Removing existing offer {} to replace it", existingOffer.getId());
                    offerRepository.delete(existingOffer);
                });

        WeeklyOffer newOffer = new WeeklyOffer(product, request.requiredQuantity(), request.offerPrice());
        WeeklyOffer savedOffer = offerRepository.save(newOffer);

        log.info("New offer created for product {}: {} for {} EUR",
                product.getName(), savedOffer.getRequiredQuantity(), savedOffer.getOfferPrice());
        return savedOffer;
    }

    @Override
    public void deleteOffer(UUID offerId) {
        log.info("Attempting to delete offer: {}", offerId);

        if (!offerRepository.existsById(offerId)) {
            log.warn("Delete failed: Offer {} not found", offerId);
            throw new EntityNotFoundException("Offer not found: " + offerId);
        }

        offerRepository.deleteById(offerId);
        log.info("Offer {} successfully deleted", offerId);
    }
}