package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OfferRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.WeeklyOffer;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<WeeklyOffer> createOffer(@RequestBody @Valid OfferRequestDTO request) {
        return ResponseEntity.status(201).body(offerService.createOrUpdateOffer(request));
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteOffer(@PathVariable UUID offerId) {
        offerService.deleteOffer(offerId);
        return ResponseEntity.noContent().build();
    }
}