package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductResponseDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.ProductRepository;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.WeeklyOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final WeeklyOfferRepository weeklyOfferRepository;

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        log.debug("Fetching all active products");
        return productRepository.findAllByIsArchivedFalse().stream()
                .map(p -> new ProductResponseDTO(p.getId(), p.getName(), p.getPrice()))
                .toList();
    }

    @Override
    public Product getProductEntityById(UUID id) {
        return productRepository.findByIdAndIsArchivedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        log.info("Creating new product with name: {}", request.name());

        Product product = new Product(request.name(), request.price());
        Product savedProduct = productRepository.save(product);

        log.info("Product successfully created with ID: {}", savedProduct.getId());
        return new ProductResponseDTO(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        log.info("Attempting to archive product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        product.setArchived(true);
        productRepository.save(product);
        log.info("Product with ID {} successfully archived", id);

        weeklyOfferRepository.findAllByProductId(id).forEach(offer -> {
            offer.setArchived(true);
            weeklyOfferRepository.save(offer);
            log.info("Cascaded archive to weekly offer with ID {}", offer.getId());
        });
    }
}