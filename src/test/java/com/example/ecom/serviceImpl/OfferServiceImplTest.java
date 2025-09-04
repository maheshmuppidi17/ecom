package com.example.ecom.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

import com.example.ecom.dto.OfferRequest;
import com.example.ecom.dto.OfferResponse;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.model.Offer;
import com.example.ecom.repo.OfferRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferServiceImplTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferServiceImpl offerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOffer_Success() {
        OfferRequest request = new OfferRequest();
        request.setCode("OFFER2025");
        request.setDiscountPercentage(20);

        Offer savedOffer = new Offer();
        savedOffer.setCode("OFFER2025");
        savedOffer.setDiscountPercentage(20);

        when(offerRepository.save(any(Offer.class))).thenReturn(savedOffer);

        OfferResponse response = offerService.createOffer(request);

        assertNotNull(response);
        assertEquals("OFFER2025", response.getCode());
        assertEquals(20, response.getDiscountPercentage());
    }

    @Test
    void testCreateOffer_NullRequest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            offerService.createOffer(null)
        );
        assertEquals("Offer cannot be null", exception.getMessage());
    }

    @Test
    void testCreateOffer_MissingCode() {
        OfferRequest request = new OfferRequest();
        request.setDiscountPercentage(20);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            offerService.createOffer(request)
        );
        assertEquals("Offer code is required", exception.getMessage());
    }

    @Test
    void testCreateOffer_InvalidDiscount() {
        OfferRequest request = new OfferRequest();
        request.setCode("OFFER2025");
        request.setDiscountPercentage(95); // Invalid

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            offerService.createOffer(request)
        );
        assertEquals("Discount must be between 1 and 90", exception.getMessage());
    }

    @Test
    void testCreateOffer_DuplicateCode() {
        OfferRequest request = new OfferRequest();
        request.setCode("OFFER2025");
        request.setDiscountPercentage(20);

        when(offerRepository.save(any(Offer.class))).thenThrow(new DuplicateKeyException("Duplicate"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            offerService.createOffer(request)
        );
        assertEquals("Offer code already exists", exception.getMessage());
    }

    @Test
    void testGetAllOffers() {
        Offer offer1 = new Offer();
        offer1.setCode("OFFER1");

        Offer offer2 = new Offer();
        offer2.setCode("OFFER2");

        when(offerRepository.findAll()).thenReturn(Arrays.asList(offer1, offer2));

        List<Offer> offers = offerService.getAllOffers();

        assertEquals(2, offers.size());
        assertEquals("OFFER1", offers.get(0).getCode());
    }

    @Test
    void testGetOfferByCode_Success() {
        Offer offer = new Offer();
        offer.setCode("OFFER2025");

        when(offerRepository.findByCode("OFFER2025")).thenReturn(Optional.of(offer));

        Offer result = offerService.getOfferByCode("OFFER2025");

        assertNotNull(result);
        assertEquals("OFFER2025", result.getCode());
    }

    @Test
    void testGetOfferByCode_NotFound() {
        when(offerRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            offerService.getOfferByCode("INVALID")
        );
        assertEquals("Offer not found: INVALID", exception.getMessage());
    }
}
