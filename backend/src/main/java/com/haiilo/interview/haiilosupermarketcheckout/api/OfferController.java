package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.OfferRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.api.dto.WeeklyOfferDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OfferController {
    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<WeeklyOfferDTO> createOffer(@RequestBody @Valid OfferRequestDTO request) {
        return ResponseEntity.status(201).body(offerService.createOrUpdateOffer(request));
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteOffer(@PathVariable UUID offerId) {
        offerService.deleteOffer(offerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<WeeklyOfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }
}