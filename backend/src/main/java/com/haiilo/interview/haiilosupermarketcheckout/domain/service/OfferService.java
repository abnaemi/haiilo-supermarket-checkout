package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OfferRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import java.util.UUID;

public interface OfferService {

    WeeklyOffer createOrUpdateOffer(OfferRequestDTO request);
    void deleteOffer(UUID offerId);
}