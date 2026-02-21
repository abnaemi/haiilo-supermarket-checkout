package com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence;

import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeeklyOfferRepository extends JpaRepository<WeeklyOffer, UUID> {
    Optional<WeeklyOffer> findFirstByProductId(UUID productId);
    Optional<WeeklyOffer> findFirstByProductIdAndIsArchivedFalse(UUID productId);
    List<WeeklyOffer> findAllByIsArchivedFalse();
    List<WeeklyOffer> findAllByProductId(UUID productId);
}