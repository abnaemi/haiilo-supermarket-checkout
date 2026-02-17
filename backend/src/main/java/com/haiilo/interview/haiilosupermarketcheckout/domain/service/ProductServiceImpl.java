package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductResponseDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.ProductRepository;
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

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        log.debug("Fetching all products");
        return productRepository.findAll().stream()
                .map(p -> new ProductResponseDTO(p.getId(), p.getName(), p.getPrice()))
                .toList();
    }

    @Override
    public Product getProductEntityById(UUID id) {
        return productRepository.findById(id)
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
}