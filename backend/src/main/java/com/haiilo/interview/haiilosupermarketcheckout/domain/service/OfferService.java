package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OfferRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.WeeklyOfferDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;

import java.util.List;
import java.util.UUID;

public interface OfferService {
    WeeklyOfferDTO createOrUpdateOffer(OfferRequestDTO request);

    void deleteOffer(UUID offerId);

    List<WeeklyOfferDTO> getAllOffers();
}