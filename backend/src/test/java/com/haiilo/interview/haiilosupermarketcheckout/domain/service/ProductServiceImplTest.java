package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductResponseDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createProduct_ShouldSaveAndReturnDto() {
        ProductRequestDTO request = new ProductRequestDTO("Apple", BigDecimal.valueOf(0.30));
        Product savedProduct = new Product("Apple", BigDecimal.valueOf(0.30));
        savedProduct.setId(UUID.randomUUID());

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDTO result = productService.createProduct(request);

        assertThat(result.name()).isEqualTo("Apple");
        assertThat(result.id()).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProductEntityById_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductEntityById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }
}