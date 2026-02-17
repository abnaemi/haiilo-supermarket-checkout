package com.haiilo.interview.haiilosupermarketcheckout.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WeeklyOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int requiredQuantity;
    private BigDecimal offerPrice;

    public WeeklyOffer(Product product, int requiredQuantity, BigDecimal offerPrice) {
        this.product = product;
        this.requiredQuantity = requiredQuantity;
        this.offerPrice = offerPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeeklyOffer other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}