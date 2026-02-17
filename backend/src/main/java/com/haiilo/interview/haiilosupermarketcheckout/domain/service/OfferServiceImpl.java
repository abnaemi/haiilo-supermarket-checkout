package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OfferRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.WeeklyOfferDTO; // Neu
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.WeeklyOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OfferServiceImpl implements OfferService {

    private final WeeklyOfferRepository offerRepository;
    private final ProductService productService;

    @Override
    public WeeklyOfferDTO createOrUpdateOffer(OfferRequestDTO request) {
        log.info("Processing offer request for product ID: {}", request.productId());

        Product product = productService.getProductEntityById(request.productId());

        offerRepository.findFirstByProductId(product.getId())
                .ifPresent(existingOffer -> {
                    log.debug("Removing existing offer {} to replace it", existingOffer.getId());
                    offerRepository.delete(existingOffer);
                    offerRepository.flush();
                });

        WeeklyOffer newOffer = new WeeklyOffer(product, request.requiredQuantity(), request.offerPrice());
        WeeklyOffer savedOffer = offerRepository.save(newOffer);

        log.info("New offer created for product {}: {} for {} EUR",
                product.getName(), savedOffer.getRequiredQuantity(), savedOffer.getOfferPrice());

        return mapToDTO(savedOffer);
    }

    @Override
    public void deleteOffer(UUID offerId) {
        log.info("Attempting to delete offer: {}", offerId);
        if (!offerRepository.existsById(offerId)) {
            log.warn("Delete failed: Offer {} not found", offerId);
            throw new EntityNotFoundException("Offer not found: " + offerId);
        }
        offerRepository.deleteById(offerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeeklyOfferDTO> getAllOffers() {
        log.debug("Fetching all active weekly offers and mapping to DTO");
        return offerRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    private WeeklyOfferDTO mapToDTO(WeeklyOffer offer) {
        return new WeeklyOfferDTO(
                offer.getId(),
                offer.getProduct().getId(),
                offer.getProduct().getName(),
                offer.getRequiredQuantity(),
                offer.getOfferPrice()
        );
    }
}