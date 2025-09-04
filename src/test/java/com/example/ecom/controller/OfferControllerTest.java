package com.example.ecom.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;

import com.example.ecom.contrller.OfferController;
import com.example.ecom.dto.AppResponseDto;
import com.example.ecom.dto.OfferRequest;
import com.example.ecom.dto.OfferResponse;
import com.example.ecom.model.Offer;
import com.example.ecom.service.OfferService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferControllerTest {

    @Mock
    private OfferService offerService;

    @InjectMocks
    private OfferController offerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOffer() {
        OfferRequest request = new OfferRequest();
        request.setCode("OFFER2025");
        request.setDiscountPercentage(20);

        OfferResponse response = new OfferResponse();
        response.setCode("OFFER2025");
        response.setDiscountPercentage(20);

        when(offerService.createOffer(request)).thenReturn(response);

        ResponseEntity<AppResponseDto> result = offerController.createOffer(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Offer code created successfully", result.getBody().getMessage());
        assertEquals("OFFER2025", ((OfferResponse) result.getBody().getData()).getCode());
    }

    @Test
    void testGetByCode() {
        Offer offer = new Offer();
        offer.setCode("OFFER2025");
        offer.setDiscountPercentage(20);

        when(offerService.getOfferByCode("OFFER2025")).thenReturn(offer);

        ResponseEntity<AppResponseDto> result = offerController.getByCode("OFFER2025");

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Offer Code", result.getBody().getMessage());
        assertEquals("OFFER2025", ((Offer) result.getBody().getData()).getCode());
    }

    @Test
    void testGetAllOffers() {
        Offer offer1 = new Offer();
        offer1.setCode("OFFER1");
        offer1.setDiscountPercentage(10);

        Offer offer2 = new Offer();
        offer2.setCode("OFFER2");
        offer2.setDiscountPercentage(15);

        List<Offer> offers = Arrays.asList(offer1, offer2);

        when(offerService.getAllOffers()).thenReturn(offers);

        ResponseEntity<List<Offer>> result = offerController.getAllOffers();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2, result.getBody().size());
        assertEquals("OFFER1", result.getBody().get(0).getCode());
    }
}
