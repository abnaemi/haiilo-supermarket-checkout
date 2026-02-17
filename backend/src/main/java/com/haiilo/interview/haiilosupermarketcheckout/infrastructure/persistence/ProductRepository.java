package com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}