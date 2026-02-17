package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ProductResponseDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    Product getProductEntityById(UUID id);
    ProductResponseDTO createProduct(ProductRequestDTO request);
    void deleteProduct(UUID id);
}